package org.vitramu.engine.excution.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.vitramu.engine.excution.service.FlowStateMachineService;

import java.util.Map;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitMqConfiguration {
    private static final String eventExchangeName = "event-exchange";
    private static final String taskQueueName = "task-queue";
    private static final String startQueueName = "start-queue";
    private static final String eventRoutingKey = "vitramu.orchestrator.task";

    @Bean
    public Queue queue() {
        return new Queue(taskQueueName, false);
    }

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange(eventExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(eventRoutingKey);
    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(taskQueueName);
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
    @RabbitListener(queues = {taskQueueName})
    public void processTaskEvent(@Payload Map<String, String> data, @Header("event_type") String eventType) {
        log.info("receive message: {}", data);
        flowService.completeTask(eventType);
    }
}
