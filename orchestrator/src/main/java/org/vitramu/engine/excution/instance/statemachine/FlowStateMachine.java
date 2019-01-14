package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;
import org.vitramu.common.constant.DefinitionState;
import org.vitramu.engine.excution.action.FlowStateAction;
import org.vitramu.engine.excution.action.FlowStateEntryAction;
import org.vitramu.engine.excution.action.FlowStateExitAction;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;

import java.util.HashMap;

@Slf4j
public class FlowStateMachine {


    public static final String EVENT_INITIALIZED = "INITIALIZED";

    public static StateMachine<String, String> newStateMachineInstance(String machineId, ConnectionFactory connectionFactory) {
        FlowStateEntryAction entryAction = new FlowStateEntryAction(connectionFactory);
        FlowStateAction stateAction = new FlowStateAction();
        FlowStateExitAction exitAction = new FlowStateExitAction();

        StateMachine<String, String> OSB_PUD_EW_SM = null;
        try {
            StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
            builder.configureStates()
                    .withStates()
                    .initial(DefinitionState.REQUEST_ARRIVED.getName())
                    .state(DefinitionState.REQUEST_SAVING.getName(), entryAction, exitAction)
                    .end(DefinitionState.END.getName())
                    .fork(DefinitionState.CREATE_PARALLEL.getName())
                    .join(DefinitionState.CREATE_FINISH.getName())
//                    .choice(DefinitionState.CHOICE_EW.getName())
                    .state(DefinitionState.CREATE_BOOKING.getName(), entryAction, exitAction)
                    .and()
                    .withStates()
                    .parent(DefinitionState.CREATE_BOOKING.getName()).initial(DefinitionState.CREATE_OSB.getName()).state(DefinitionState.CREATE_OSB.getName(), entryAction, exitAction).end(DefinitionState.END_OSB.getName())
                    .and()
                    .withStates()
                    .parent(DefinitionState.CREATE_BOOKING.getName()).initial(DefinitionState.CREATE_PUD.getName()).state(DefinitionState.CREATE_PUD.getName(), entryAction, exitAction).end(DefinitionState.END_PUD.getName())
                    .and()
                    .withStates().state(DefinitionState.REFRESH_STATUS.getName(), entryAction, exitAction);
//                    .withStates().state(DefinitionState.CREATE_EW.getName(), entryAction, exitAction)

            builder.configureTransitions()
                    .withExternal().source(DefinitionState.REQUEST_ARRIVED.getName()).target(DefinitionState.REQUEST_SAVING.getName()).event(EVENT_INITIALIZED)
                    .and().withExternal().source(DefinitionState.REQUEST_SAVING.getName()).target(DefinitionState.CREATE_PARALLEL.getName()).event(DefinitionState.REQUEST_SAVING.getName())
                    .and().withFork().source(DefinitionState.CREATE_PARALLEL.getName()).target(DefinitionState.CREATE_OSB.getName()).target(DefinitionState.CREATE_PUD.getName())
                    .and().withExternal().source(DefinitionState.CREATE_OSB.getName()).target(DefinitionState.END_OSB.getName()).event(DefinitionState.CREATE_OSB.getName())
                    .and().withExternal().source(DefinitionState.CREATE_PUD.getName()).target(DefinitionState.END_PUD.getName()).event(DefinitionState.CREATE_PUD.getName())
                    .and().withJoin().source(DefinitionState.END_OSB.getName()).source(DefinitionState.END_PUD.getName()).target(DefinitionState.CREATE_FINISH.getName())
//                    .and().withExternal().source(DefinitionState.CREATE_FINISH.getName()).target(DefinitionState.CHOICE_EW.getName())
//                    .and().withChoice().source(DefinitionState.CHOICE_EW.getName()).first(DefinitionState.CREATE_EW.getName(), createEwGuard()).last(DefinitionState.REFRESH_STATUS.getName())
//                    .and().withExternal().source(DefinitionState.CREATE_EW.getName()).target(DefinitionState.REFRESH_STATUS.getName()).event(DefinitionState.CREATE_EW.getName())
                    .and().withExternal().source(DefinitionState.CREATE_FINISH.getName()).target(DefinitionState.REFRESH_STATUS.getName())
                    .and().withExternal().source(DefinitionState.REFRESH_STATUS.getName()).target(DefinitionState.END.getName());
            builder.configureConfiguration()
                    .withConfiguration().machineId(machineId);

            OSB_PUD_EW_SM = builder.build();
            OSB_PUD_EW_SM.addStateListener(new FlowEngineEventListener());
        } catch (Exception e) {
            log.error("factory build failed", e);
        }
        return OSB_PUD_EW_SM;

    }

    private static Guard<String, String> createEwGuard() {
        return context -> {
            log.info("evaluate ew guard");
            return true;
        };
    }

    public static class InMemoryStateMachinePersister extends AbstractStateMachinePersister<String, String, String> {
        public InMemoryStateMachinePersister() {
            super(new InMemoryStateMachinePersist());
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
