package org.vitramu.engine.excution.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.vitramu.engine.excution.service.FlowStateMachineService;

import java.util.Map;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitMqConfiguration {
    public static final String EVENT_EXCHANGE_NAME = "vitramu.exchange.event";
    public static final String COMMAND_EXCHANGE_NAME = "vitramu.exchange.command";
    public static final String TASK_QUEUE_NAME = "vitramu.orchestrator.task";
    public static final String START_QUEUE_NAME = "vitramu.orchestrator.start";
    public static final String EVENT_ROUTING_KEY = "vitramu.orchestrator.task";


    @Bean("TaskQueue")
    public Queue taskQueue() {
        return new Queue(TASK_QUEUE_NAME, false);
    }

    @Bean("StartQueue")
    public Queue startQueue() {
        return new Queue(START_QUEUE_NAME, false);
    }

    @Bean("EventExchange")
    public DirectExchange eventExchange() {
        return new DirectExchange(EVENT_EXCHANGE_NAME);
    }

    @Bean("CommandExchange")
    public DirectExchange commandExchange() {
        return new DirectExchange(COMMAND_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(@Qualifier("TaskQueue") Queue queue, @Qualifier("EventExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EVENT_ROUTING_KEY);
    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(TASK_QUEUE_NAME);
//        container.setMessageListener(messageListener());
//        return container;
//    }
//
//    public MessageListener messageListener() {
//        return new MessageListener() {
//            @Override
//            public void onMessage(Message message) {
//                System.out.println("message = [" + message + "]");
//                log.info("receive message: {}", message);
//            }
//        };
//    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Autowired
    private FlowStateMachineService flowService;

    @RabbitListener(queues = {TASK_QUEUE_NAME})
    public void processTaskEvent(@Payload Map<String, String> data, @Header("eventType") String eventType) {
        log.info("receive message: {}", data);
        flowService.completeTask(eventType);
    }

    @RabbitListener(queues = {START_QUEUE_NAME})
    public void processStartEvent(@Payload Map<String, String> data, @Header("flowId") String flowId) {
        log.info("receive message: {}", data);
    }

}
