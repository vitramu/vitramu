package org.vitramu.engine.excution.message;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class StartMessage extends FlowMessage {

    private String parentFlowInstanceId; // assigned by orchestrator

    @Builder
    public StartMessage(String flowDefinitionId, String flowInstanceId, String parentFlowInstanceId, String serviceName, String serviceInstanceId, JsonElement body) {
        super(flowDefinitionId, flowInstanceId, serviceName, serviceInstanceId, body);
        this.parentFlowInstanceId = parentFlowInstanceId;
    }

    public static StartMessageBuilder builder(FlowMessage message) {
        return new StartMessageBuilder().flowDefinitionId(message.getFlowDefinitionId())
                .flowInstanceId(message.flowInstanceId)
                .serviceName(message.getServiceName())
                .serviceInstanceId(message.getServiceInstanceId())
                .body(message.getBody());
    }

    public static StartMessageBuilder builder() {
        return new StartMessageBuilder();
    }
}
