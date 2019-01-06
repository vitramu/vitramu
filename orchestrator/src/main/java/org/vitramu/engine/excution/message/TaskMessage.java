package org.vitramu.engine.excution.service;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskMessage extends FlowMessage {

    private String taskName;
    private String taskInstanceId;
    private boolean aborted;

    private JsonElement body;

    @Builder
    public TaskMessage(String flowDefinitionId, String flowInstanceId, String taskName, String taskInstanceId, boolean aborted, JsonElement body) {
        super(flowDefinitionId, flowInstanceId);
        this.taskName = taskName;
        this.taskInstanceId = taskInstanceId;
        this.aborted = aborted;
        this.body = body;
    }
}
