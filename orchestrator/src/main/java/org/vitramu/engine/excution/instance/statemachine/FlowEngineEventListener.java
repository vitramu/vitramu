package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.vitramu.engine.definition.element.TaskDefinition;

import static java.util.stream.Collectors.toList;

@Slf4j
public class FlowStateMachineEventListener extends StateMachineListenerAdapter<TaskDefinition, String> {
    @Override
    public void stateEntered(State<TaskDefinition, String> state) {
        log.info("Enter State: {}", state.getIds().stream().map(t -> t.getName()).collect(toList()));
    }

    @Override
    public void stateExited(State<TaskDefinition, String> state) {
        log.info("Exit State: {}", state.getIds().stream().map(t -> t.getName()).collect(toList()));
    }

}
