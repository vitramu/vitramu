package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlowStateMachineInterceptor implements StateMachineInterceptor {

    @Override
    public Message preEvent(Message message, StateMachine stateMachine) {
        log.info("preEvent with message={}", message);
        return message;
    }

    @Override
    public void preStateChange(State state, Message message, Transition transition, StateMachine stateMachine) {
    }

    @Override
    public void postStateChange(State state, Message message, Transition transition, StateMachine stateMachine) {
    }

    @Override
    public StateContext preTransition(StateContext stateContext) {
        return stateContext;
    }

    @Override
    public StateContext postTransition(StateContext stateContext) {
        return stateContext;
    }

    @Override
    public Exception stateMachineError(StateMachine stateMachine, Exception exception) {
        log.error("state machine error with exeption={}", exception);
        return exception;
    }
}
