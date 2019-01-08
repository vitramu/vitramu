package org.vitramu.engine.excution.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;

@Slf4j
@Component
public class FlowTransitionErrorAction implements Action<String, String> {
    @Override
    public void execute(StateContext<String, String> context) {
        log.error("excuting error...");
//        TODO default flow transition error action
//        abort every executed task ordered by exit time reverse
    }
}
