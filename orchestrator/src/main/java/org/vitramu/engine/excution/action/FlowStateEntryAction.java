package org.vitramu.engine.excution.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.vitramu.common.definition.element.TaskDefinition;

import java.util.HashMap;
import java.util.Map;

import static org.vitramu.common.constant.MqConstant.COMMAND_EXCHANGE_NAME;


/**
 * FlowStateEntryAction 在进入state时向微服务下发command
 *
 */
@Slf4j
@Component
public class FlowStateEntryAction implements Action<String, String> {

    private ConnectionFactory connectionFactory;

    @Autowired
    public FlowStateEntryAction(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        // TODO this is test logic
        log.info("Entering...");
//        Definition def = context.getStateMachine().getState().getId();
//        if (DefinitionType.TASK.equals(def.getType())) {
//            excute((TaskDefinition) def);
//        }
    }

    private void excute(TaskDefinition def) {
        log.info("Dispatching...");
        Map<String, String> payload = new HashMap<>(4);
        payload.put("data", "something");

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setExchange(COMMAND_EXCHANGE_NAME);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.convertAndSend("vitramu.service.service1", payload, m -> {
            m.getMessageProperties().getHeaders().put("taskName", def.getName());
            m.getMessageProperties().getHeaders().put("taskId", def.getId());
            m.getMessageProperties().setContentType("application/json");
            return m;
        });
    }
}
