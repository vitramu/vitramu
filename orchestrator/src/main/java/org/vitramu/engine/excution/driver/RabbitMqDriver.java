package org.vitramu.engine.excution.driver;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vitramu.engine.excution.instance.FlowInstance;
import org.vitramu.engine.excution.instance.FlowInstanceService;
import org.vitramu.engine.excution.message.FlowMessage;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import static org.vitramu.common.constant.MqConstant.*;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitMqDriver implements FlowDriver {
    @Bean
    public Queue taskQueue() {
        return new Queue(EVENT_QUEUE_NAME, false);
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
    public Binding taskMessageBinding(Queue queue, @Qualifier("EventExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EVENT_ROUTING_KEY);
    }

    @Autowired
    private FlowInstanceService flowInstanceService;

    /**
     * 监听rabbitmq消息。消息分为两类，表示task执行成功结束的消息和表示task执行失败的消息。
     * <p>
     * 消息体，Command worker执行command后产生的数据
     * 消息头，由vitramu系统使用的元数据。包括：
     * - flowDefinitionId 表示flow定义
     * - flowInstanceId 表示flow实例，相当于global transaction id
     * - taskId 表示flow中具体task的定义
     * - taskInstanceId 表示执行的task实例，相当于local transaction id
     */
    @Override
    @RabbitListener(queues = {EVENT_QUEUE_NAME})
    public void onMessage(final Message message) {
        try {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            byte[] body = message.getBody();
            String encoding = Optional.ofNullable(message.getMessageProperties().getContentEncoding()).orElse(Charset.defaultCharset().name());
            String contentType = Optional.ofNullable(message.getMessageProperties().getContentType()).orElse("application/json");
            String payload = null;
            JsonElement content = null;
            payload = new String(body, encoding);
            if (!contentType.contains("json")) {
                log.error("unsupported message content type");
            }
            content = new JsonParser().parse(payload);
            String flowDefinitionId = (String) headers.get("flowDefinitionId");
            String flowInstanceId = (String) headers.get("flowInstanceId");
            String serviceName = (String) headers.get("serviceName");
            String serviceInstanceId = (String) headers.get("serviceInstanceId");
            log.info("===========================================");
            log.info("Machine {} receives message", flowDefinitionId);

            FlowMessage flowMessage = new FlowMessage((String) headers.get("flowDefinitionId"),
                    (String) headers.get("flowInstanceId"),
                    (String) headers.get("serviceName"),
                    (String) headers.get("serviceInstanceId"),
                    content
            );
            FlowInstance instance = flowInstanceService.getFlowInstance(flowDefinitionId, flowInstanceId);
            boolean isStart = (Boolean) Optional.ofNullable(headers.get("start")).orElse(false);
            if (isStart) {
                // TODO prevent start same instance multiple times
                instance.setStartServiceName(serviceName);
                instance.setStartServiceInstanceId(serviceInstanceId);
                instance.setParentInstanceId(null);
                instance.start();
            } else {
                boolean isAborted = (Boolean) Optional.ofNullable(headers.get("aborted")).orElse(false);
                String taskInstanceId = (String) headers.get("taskInstanceId");
                String taskName = (String) headers.get("taskName");
                if (isAborted) {
                    log.info("Aborting with taskName={} and taskInstanceId={}", taskName, taskInstanceId);
//                    TODO abort task
                } else {
                    log.info("Completing task with taskName={} and taskInstanceId={}", taskName, taskInstanceId);
                    instance.completeTask(taskInstanceId, taskName);
                }
            }
            if (instance.isEnded()) {
                flowInstanceService.finish(instance);
            } else {
                flowInstanceService.pause(instance);
            }


        } catch (Exception e) {
            log.error("rabbit listener exception", e);
        }

    }
}
