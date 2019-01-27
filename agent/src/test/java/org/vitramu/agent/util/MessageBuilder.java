package org.vitramu.agent.util;

import org.springframework.amqp.core.Message;

import java.util.Map;

import static org.vitramu.common.constant.MqConstant.SERVICE_INSTANCE_ID;
import static org.vitramu.common.constant.MqConstant.SERVICE_NAME;

public class MessageBuilder {

    private final Map<String, Object> headers;
    private MessageBuilder(Map<String, Object> headers) {
        this.headers = headers;
    }

    public static MessageBuilder with(Map<String, Object> headers) {
        return new MessageBuilder(headers);
    }

    public MessageBuilder withFlow(String flowDefinitionId,  String flowInstanceId) {
        headers.put("flowDefinitionId", flowDefinitionId);
        headers.put("flowInstanceId", flowInstanceId);

        return this;
    }

    public MessageBuilder withService(String serviceName, String serviceInstanceId) {
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        return this;
    }

    public MessageBuilder withStart() {
        headers.put("start", true);
        return this;
    }


    public MessageBuilder withTask(String taskInstanceId, String taskName) {
        headers.put("taskInstanceId", taskInstanceId);
        headers.put("taskName", taskName);
        return this;
    }
}
