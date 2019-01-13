package org.vitramu.engine.excution.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

/**
 * FlowStateAction 负责在下发command成功后对flow 当前 state 进行持久化
 *
 * @param <S>
 * @param <E>
 */
@Slf4j
@Component
public class FlowStateAction implements Action<String, String>  {


    @Override
    public void execute(StateContext<String, String> context) {
        log.info("excuting...");
    }
}
