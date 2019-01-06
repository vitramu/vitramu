package org.vitramu.engine.excution.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlowMessage {
    protected final String flowDefinitionId; // assigned by microservice
    protected final String flowInstanceId;  // assigned by orchestrator

}
