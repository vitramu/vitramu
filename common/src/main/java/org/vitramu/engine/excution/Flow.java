package org.vitramu.engine.excution;

import org.vitramu.engine.definition.FlowDefinition;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.Type;

public class Flow {
    private static transient FlowBuilder builder;

    private final String instanceId;
    private final FlowDefinition definition;

    public Flow(String instanceId, FlowDefinition definition) {
        this.instanceId = instanceId;
        this.definition = definition;
    }

    public static FlowBuilder builder(FlowDefinitionRepository repository) {
        if(null == builder) {
            synchronized (Flow.class) {
                if(null == builder) {
                    builder = new FlowBuilder(repository);
                }
            }
        }
        return builder;
    }
    public String getInstanceId() {
        return this.instanceId;
    }

    public String getDefinitionId() {
        return this.definition.getId();
    }

    public Type getDefinitionType() {
        return this.definition.getType();
    }

    public void start() {
        // throw state exception when current flow state not supports start operation
    }

    public boolean isEnded() {
        return false;
    }

    public void completeTask(String taskId) {
        // whether taskId is in current flow defintion

        // change task state according current task state and taskId


    }

    public void abortTask(String taskId) {
        // whether taskId is in current flow defintion

        // change task state according current task state and taskId

    }

    public void abort() {
        // abort every task excuted in this flow instance reverse ordered by task completed time
    }
}
