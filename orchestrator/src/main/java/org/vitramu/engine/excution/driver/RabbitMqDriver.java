package org.vitramu.engine.excution.message;

import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.vitramu.engine.excution.service.FlowDriver;
import org.vitramu.engine.excution.instance.FlowInstanceService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitMqConfiguration implements FlowDriver {
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

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    @Autowired
    private FlowInstanceService flowService;

    /**
     * 监听rabbitmq消息。消息分为两类，表示task执行成功结束的消息和表示task执行失败的消息。
     *
     * @param payload 消息体，Command worker执行command后产生的数据
     * @param headers 消息头，由vitramu系统使用的元数据。包括：
     *                - flowDefinitionId 表示flow定义
     *                - flowInstanceId 表示flow实例，相当于global transaction id
     *                - taskId 表示flow中具体task的定义
     *                - taskInstanceId 表示执行的task实例，相当于local transaction id
     */
    @Override
    @RabbitListener(queues = {TASK_QUEUE_NAME})
    public void onTaskMessage(@Payload String payload, @Headers Map<String, Object> headers) {
        log.info("receive message: {}", payload);
        String taskInstanceId = UUID.randomUUID().toString();
        TaskMessage taskMessage = TaskMessage.builder()
                .flowDefinitionId((String) headers.get("flowDefinitionId"))
                .flowInstanceId((String) headers.get("flowInstanceId"))
                .taskInstanceId(taskInstanceId)
                .taskName((String) headers.get("flowInstanceId"))
                .aborted(Boolean.valueOf((String) headers.get("aborted")))
                .build();
        flowService.completeTask(taskMessage);
    }

    @Override
    @RabbitListener(queues = {START_QUEUE_NAME})
    public void onStartMessage(@Payload String payload, @Headers Map<String, Object> headers) {
        log.info("receive message: {}", payload);
        // flow instance started by start queue is always top level
        String flowInstanceId = UUID.randomUUID().toString();
        StartMessage start = StartMessage.builder()
                .flowDefinitionId((String) headers.get("flowDefinitionId"))
                .flowInstanceId(flowInstanceId)
                .parentFlowInstanceId(null)
                .serviceName((String) headers.get("serviceName"))
                .serviceInstanceId((String) headers.get("serviceInstanceId"))
                .body(new JsonParser().parse(payload))
                .build();
        flowService.startFlowInstance(start);
    }

}
