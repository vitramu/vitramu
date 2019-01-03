package org.vitramu.engine.excution.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@Slf4j
@Configuration
@EnableRabbit
public class DispatcherConfiguration {

    public static final String SERVICE_QUEUE_SERVICE1 = "service.service1";
    public static final String SERVICE_ROUTING_KEY_SERVICE1 = "vitramu.service.service1";

    public static void main(String[] args) {
        SpringApplication.run(DispatcherConfiguration.class, args);
    }

    @Bean("MockService1")
    public Queue service1Queue() {
        return new Queue(SERVICE_QUEUE_SERVICE1, false);
    }

    @Bean
    public Binding serviceQueueBinding(@Qualifier("MockService1") Queue queue, @Qualifier("CommandExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SERVICE_ROUTING_KEY_SERVICE1);
    }

    @RabbitListener(queues = {SERVICE_QUEUE_SERVICE1})
    public void processTaskEvent(@Payload Map<String,String> data, @Header("taskId") String taskId) {
        log.info("service receive taskId: {}, message: {}", taskId, data);
    }
}
