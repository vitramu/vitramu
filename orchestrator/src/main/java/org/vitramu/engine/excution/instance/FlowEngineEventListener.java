package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.vitramu.engine.definition.Definition;

import static java.util.stream.Collectors.toList;

@Slf4j
public class FlowEngineEventListener extends StateMachineListenerAdapter<String, String> {
    @Override
    public void stateEntered(State<String, String> state) {
        log.info("Enter State: {}", state.getIds());
    }

    @Override
    public void stateExited(State<String, String> state) {
        log.info("Exit State: {}", state.getIds());
    }

}
