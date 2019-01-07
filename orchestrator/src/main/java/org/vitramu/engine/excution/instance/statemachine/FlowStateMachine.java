package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;
import org.vitramu.engine.constant.DefinitionState;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.excution.action.FlowStateAction;
import org.vitramu.engine.excution.action.FlowStateEntryAction;
import org.vitramu.engine.excution.action.FlowStateExitAction;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;

import java.util.HashMap;

@Slf4j
public class FlowStateMachine {


    public static final String EVENT_INITIALIZED = "INITIALIZED";


    public static StateMachine<Definition, String> newStateMachineInstance(String machineId, ConnectionFactory connectionFactory) {
        FlowStateEntryAction entryAction = new FlowStateEntryAction(connectionFactory);
        FlowStateAction stateAction = new FlowStateAction();
        FlowStateExitAction exitAction = new FlowStateExitAction();

        StateMachine<Definition, String> OSB_PUD_EW_SM = null;
        try {
            StateMachineBuilder.Builder<Definition, String> builder = StateMachineBuilder.builder();
            builder.configureStates()
                    .withStates()
                    .initial(DefinitionState.REQUEST_ARRIVED)
                    .state(DefinitionState.REQUEST_SAVING, entryAction, exitAction)
                    .end(DefinitionState.END)
                    .fork(DefinitionState.CREATE_PARALLEL)
                    .join(DefinitionState.CREATE_FINISH)
                    .choice(DefinitionState.CHOICE_EW)
                    .state(DefinitionState.CREATE_BOOKING, entryAction, exitAction)
                    .and()
                    .withStates()
                    .parent(DefinitionState.CREATE_BOOKING).initial(DefinitionState.CREATE_OSB).state(DefinitionState.CREATE_OSB, entryAction, exitAction).end(DefinitionState.END_OSB)
                    .and()
                    .withStates()
                    .parent(DefinitionState.CREATE_BOOKING).initial(DefinitionState.CREATE_PUD).state(DefinitionState.CREATE_PUD, entryAction, exitAction).end(DefinitionState.END_PUD)
                    .and()
                    .withStates().state(DefinitionState.CREATE_EW, entryAction, exitAction).state(DefinitionState.REFRESH_STATUS, entryAction, exitAction);
            builder.configureTransitions()
                    .withExternal().source(DefinitionState.REQUEST_ARRIVED).target(DefinitionState.REQUEST_SAVING).event(EVENT_INITIALIZED)
                    .and().withExternal().source(DefinitionState.REQUEST_SAVING).target(DefinitionState.CREATE_PARALLEL).event(DefinitionState.REQUEST_SAVING.getName())
                    .and().withFork().source(DefinitionState.CREATE_PARALLEL).target(DefinitionState.CREATE_OSB).target(DefinitionState.CREATE_PUD)
                    .and().withExternal().source(DefinitionState.CREATE_OSB).target(DefinitionState.END_OSB).event(DefinitionState.CREATE_OSB.getName())
                    .and().withExternal().source(DefinitionState.CREATE_PUD).target(DefinitionState.END_PUD).event(DefinitionState.CREATE_PUD.getName())
                    .and().withJoin().source(DefinitionState.END_OSB).source(DefinitionState.END_PUD).target(DefinitionState.CREATE_FINISH)
                    .and().withExternal().source(DefinitionState.CREATE_FINISH).target(DefinitionState.CHOICE_EW)
                    .and().withChoice().source(DefinitionState.CHOICE_EW).first(DefinitionState.CREATE_EW, createEwGuard()).last(DefinitionState.REFRESH_STATUS)
                    .and().withExternal().source(DefinitionState.CREATE_EW).target(DefinitionState.REFRESH_STATUS).event(DefinitionState.CREATE_EW.getName())
                    .and().withExternal().source(DefinitionState.REFRESH_STATUS).target(DefinitionState.END);
            builder.configureConfiguration()
                    .withConfiguration().machineId(machineId);

            OSB_PUD_EW_SM = builder.build();
            OSB_PUD_EW_SM.addStateListener(new FlowEngineEventListener());
        } catch (Exception e) {
            log.error("factory build failed", e);
        }
        return OSB_PUD_EW_SM;

    }

    private static Guard<Definition, String> createEwGuard() {
        return context -> {
            log.info("evaluate ew guard");
            return true;
        };
    }

    public static class InMemoryStateMachinePersister<T> extends AbstractStateMachinePersister<T, String, String> {
        public InMemoryStateMachinePersister() {
            super(new InMemoryStateMachinePersist<T>());
        }
    }

    public static class InMemoryStateMachinePersist<T> implements StateMachinePersist<T, String, String> {

        private final HashMap<String, StateMachineContext<T, String>> contexts = new HashMap<>();

        @Override
        public void write(StateMachineContext<T, String> context, String contextObj) throws Exception {
            contexts.put(contextObj, context);
        }

        @Override
        public StateMachineContext<T, String> read(String contextObj) throws Exception {
            return contexts.get(contextObj);
        }
    }
}
