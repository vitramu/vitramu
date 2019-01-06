package org.vitramu.engine.excution.service;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StartMessage extends FlowMessage {

    private String serviceName; // assigned by microservice
    private String serviceInstanceId; // assigned by microservice
    private String parentFlowInstanceId; // assigned by orchestrator
    private JsonElement body;

    @Builder
    public StartMessage(String flowDefinitionId, String flowInstanceId, String parentFlowInstanceId, String serviceName, String serviceInstanceId, JsonElement body) {
        super(flowDefinitionId, flowInstanceId);
        this.serviceName = serviceName;
        this.serviceInstanceId = serviceInstanceId;
        this.parentFlowInstanceId = parentFlowInstanceId;
        this.body = body;
    }
}
