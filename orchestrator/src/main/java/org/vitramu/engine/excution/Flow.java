package org.vitramu.engine.excution;

import lombok.*;
import org.vitramu.engine.definition.element.DefinitionType;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.excution.element.*;

import java.util.List;

@ToString
public class Flow extends AbstractExcutableInstance<FlowDefinition> implements FlowInstance {

    @NonNull
    @Getter
    private String instanceId;
    private List<Task> tasks;
    private List<Gateway> gateways;
    private List<Sequence> sequences;
    @Getter
    private String startServiceName;
    @Getter
    private String startServiceInstanceId;
    @Getter
    private String parentFlowInstanceId;
    @Getter
    private String response;

    @Builder
    public Flow(String instanceId, String parentFlowInstanceId, String startServiceName, String startServiceInstanceId, FlowDefinition definition) {
        this.instanceId = instanceId;
        this.definition = definition;
    }

    public Flow(final String instanceId) {
        this(instanceId, null, null, null, null);
    }


    @Override
    public String getDefinitionId() {
        return definition.getId();
    }

    public DefinitionType getDefinitionType() {
        return this.definition.getType();
    }


    public void start() {
        super.start();
        // throw state exception when current flow state not supports start operation
    }


    public void finish() {
        super.start();
    }

    public boolean isEnded() {
        return false;
    }

    @Override
    public void completeTask(String taskId) {
        // whether taskId is in current flow defintion

        // change task state according current task state and taskId


    }

    @Override
    public void abortTask(String taskId) {
        // whether taskId is in current flow defintion

        // change task state according current task state and taskId

    }

    @Override
    public void abort() {
        // abort every task excuted in this flow instance reverse ordered by task completed time
    }

    public static class FlowBuilder {

        public FlowBuilder starter(StartEvent starter) {
            return this.parentFlowInstanceId(starter.getParentFlowInstanceId())
                    .instanceId(starter.getTransactionId())
                    .startServiceInstanceId(starter.getServiceInstanceId())
                    .startServiceName(starter.getServiceName());
        }

    }

}
