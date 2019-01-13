package org.vitramu.engine.excution.instance;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.statemachine.StateMachine;

@Data
@AllArgsConstructor
public class FlowEngine {
    private String definitionId;
    private StateMachine<String, String> stateMachine;


    public void sendEvent(String event) {
        stateMachine.sendEvent(event);
    }
}
