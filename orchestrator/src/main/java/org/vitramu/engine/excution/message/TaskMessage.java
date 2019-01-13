package org.vitramu.engine.excution.message;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Deprecated
@Getter
@ToString(callSuper = true)
public class TaskMessage extends FlowMessage {

    private String taskName;
    private String taskInstanceId;
    private boolean aborted = false;


    @Builder
    public TaskMessage(String flowDefinitionId, String flowInstanceId, String serviceName, String serviceInstanceId, String taskName, String taskInstanceId, boolean aborted, JsonElement body) {
        super(flowDefinitionId, flowInstanceId, serviceName, serviceInstanceId, body);
        this.taskName = taskName;
        this.taskInstanceId = taskInstanceId;
        this.aborted = aborted;
        this.body = body;
    }

    public static TaskMessageBuilder builder() {
        return new TaskMessageBuilder();
    }

    public static TaskMessageBuilder builder(FlowMessage message) {
        return new TaskMessageBuilder().flowDefinitionId(message.getFlowDefinitionId())
                .flowInstanceId(message.flowInstanceId)
                .serviceName(message.getServiceName())
                .serviceInstanceId(message.getServiceInstanceId())
                .body(message.getBody());
    }
}
