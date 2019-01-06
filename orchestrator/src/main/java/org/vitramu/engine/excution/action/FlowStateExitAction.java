package org.vitramu.engine.excution.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.vitramu.engine.excution.driver.RabbitMqDriver.COMMAND_EXCHANGE_NAME;

@Slf4j
@Component
public class FlowStateEntryAction<S, E> implements Action<S, E> {

    private ConnectionFactory connectionFactory;

    @Autowired
    public FlowStateEntryAction(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void execute(StateContext<S, E> context) {
        // TODO this is test logic
        // dispatch command
        log.info("Dispatching command");
        Map<String, String> payload = new HashMap<>(4);
        payload.put("data", "something");

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setExchange(COMMAND_EXCHANGE_NAME);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.convertAndSend("vitramu.service.service1", payload, m -> {
            m.getMessageProperties().getHeaders().put("taskId", "T1");
            m.getMessageProperties().setContentType("application/json");
            return m;
        });
    }
}
