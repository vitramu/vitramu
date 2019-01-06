package org.vitramu.engine.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.excution.action.FlowStateAction;
import org.vitramu.engine.excution.action.FlowStateEntryAction;
import org.vitramu.engine.excution.action.FlowStateExitAction;
import org.vitramu.engine.excution.instance.statemachine.FlowEngineEventListener;

@Slf4j
public class FlowStateMachine {


    public static final String EVENT_INITIALIZED = "INITIALIZED";


    public static StateMachine<Definition, String> newStateMachineInstance(ConnectionFactory connectionFactory) {
        FlowStateEntryAction entryAction = new FlowStateEntryAction(connectionFactory);
        FlowStateAction stateAction = new FlowStateAction();
        FlowStateExitAction exitAction = new FlowStateExitAction();

        StateMachine<Definition, String> OSB_PUD_EW_SM = null;
        try {
            StateMachineBuilder.Builder<Definition, String> builder = StateMachineBuilder.builder();
            builder.configureStates()
                    .withStates()
                    .initial(TaskDefinitionState.REQUEST_ARRIVED)
                    .state(TaskDefinitionState.REQUEST_SAVING, entryAction, exitAction)
                    .end(TaskDefinitionState.END)
                    .fork(TaskDefinitionState.CREATE_PARALLEL)
                    .join(TaskDefinitionState.CREATE_FINISH)
                    .choice(TaskDefinitionState.CHOICE_EW)
                    .state(TaskDefinitionState.CREATE_BOOKING, entryAction, exitAction)
                    .and()
                    .withStates()
                    .parent(TaskDefinitionState.CREATE_BOOKING).initial(TaskDefinitionState.CREATE_OSB).state(TaskDefinitionState.CREATE_OSB, entryAction, exitAction).end(TaskDefinitionState.END_OSB)
                    .and()
                    .withStates()
                    .parent(TaskDefinitionState.CREATE_BOOKING).initial(TaskDefinitionState.CREATE_PUD).state(TaskDefinitionState.CREATE_PUD, entryAction, exitAction).end(TaskDefinitionState.END_PUD)
                    .and()
                    .withStates().state(TaskDefinitionState.CREATE_EW, entryAction, exitAction).state(TaskDefinitionState.REFRESH_STATUS, entryAction, exitAction);
            builder.configureTransitions()
                    .withExternal().source(TaskDefinitionState.REQUEST_ARRIVED).target(TaskDefinitionState.REQUEST_SAVING).event(EVENT_INITIALIZED)
                    .and().withExternal().source(TaskDefinitionState.REQUEST_SAVING).target(TaskDefinitionState.CREATE_PARALLEL).event(TaskDefinitionState.REQUEST_SAVING.getName())
                    .and().withFork().source(TaskDefinitionState.CREATE_PARALLEL).target(TaskDefinitionState.CREATE_OSB).target(TaskDefinitionState.CREATE_PUD)
                    .and().withExternal().source(TaskDefinitionState.CREATE_OSB).target(TaskDefinitionState.END_OSB).event(TaskDefinitionState.CREATE_OSB.getName())
                    .and().withExternal().source(TaskDefinitionState.CREATE_PUD).target(TaskDefinitionState.END_PUD).event(TaskDefinitionState.CREATE_PUD.getName())
                    .and().withJoin().source(TaskDefinitionState.END_OSB).source(TaskDefinitionState.END_PUD).target(TaskDefinitionState.CREATE_FINISH)
                    .and().withExternal().source(TaskDefinitionState.CREATE_FINISH).target(TaskDefinitionState.CHOICE_EW)
                    .and().withChoice().source(TaskDefinitionState.CHOICE_EW).first(TaskDefinitionState.CREATE_EW, createEwGuard()).last(TaskDefinitionState.REFRESH_STATUS)
                    .and().withExternal().source(TaskDefinitionState.CREATE_EW).target(TaskDefinitionState.REFRESH_STATUS).event(TaskDefinitionState.CREATE_EW.getName())
                    .and().withExternal().source(TaskDefinitionState.REFRESH_STATUS).target(TaskDefinitionState.END);
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
}
