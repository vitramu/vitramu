package org.vitramu.engine.excution.message;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class FlowMessage {
    protected final String flowDefinitionId; // assigned by microservice
    protected final String flowInstanceId;  // assigned by orchestrator
    protected String serviceName; // assigned by microservice
    protected String serviceInstanceId; // assigned by microservice
    protected JsonElement body;

}
