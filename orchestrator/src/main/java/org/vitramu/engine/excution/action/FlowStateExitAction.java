package org.vitramu.engine.excution.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;

/**
 * FlowStateExitAction 对微服务返回的task执行结果进行转换
 *
 * @param <S>
 * @param <E>
 */
@Slf4j
@Component
public class FlowStateExitAction implements Action<Definition, String> {

    private ConnectionFactory connectionFactory;

    @Override
    public void execute(StateContext<Definition, String> context) {
        // TODO this is test logic
        // dispatch command
        log.info("Exiting...");
    }
}
