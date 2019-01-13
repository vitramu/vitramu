package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachinePersister;

@Slf4j
public class FlowStateMachineStartListener extends StateMachineListenerAdapter<String, String> {

    private final StateMachinePersister<String, String, String> stateMachinePersister;

    private String context;

    public FlowStateMachineStartListener(String context, StateMachinePersister<String, String, String> stateMachinePersister) {
        this.context = context;
        this.stateMachinePersister = stateMachinePersister;
    }

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        if (stateContext.getStage().equals(StateContext.Stage.STATEMACHINE_START)) {
            try {
                stateMachinePersister.persist(stateContext.getStateMachine(), context);
            } catch (Exception e) {
                log.error("Persist context errors with contextObj={}, exception={}", context, e);
            }
        }
    }
}
