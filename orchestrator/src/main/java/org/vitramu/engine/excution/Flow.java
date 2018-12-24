package org.vitramu.engine.excution;

import lombok.*;
import org.vitramu.engine.definition.element.DefinitionType;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.definition.element.GatewayDefinition;
import org.vitramu.engine.definition.element.SequenceDefinition;
import org.vitramu.engine.excution.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
public class Flow extends AbstractExcutableInstance<FlowDefinition> implements FlowInstance {

    @NonNull
    @Getter
    private String instanceId;
    private List<Task> tasks = new ArrayList<>();
    private List<Gateway> gateways = new ArrayList<>();
    private List<Sequence> sequences = new ArrayList<>();
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

    @Override
    public DefinitionType getDefinitionType() {
        return this.definition.getType();
    }


    @Override
    public void start() {
        super.start();
        // throw state exception when current flow state not supports start operation
        SequenceDefinition seqDef = this.definition.findSequenceByStart();
        this.walkThrough(seqDef);
    }

    protected void walkThrougnGateway(@NonNull String gatewayId) {
        this.definition.findGatewayDefinition(gatewayId).ifPresent(gatewayDefinition -> {
            List<SequenceDefinition> seqDefs = this.definition.findSequenceByTarget(gatewayId);
            switch (gatewayDefinition.getGatewayType()) {
                case PARALLEL:
                    this.gateways.add(new ParallelGateway());
                    seqDefs.stream().forEach(this::walkThrough);
                    break;
                case EXCLUSIVE:
                case INCLUSIVE:
                case JOIN:
                    Optional<Gateway> walkedThroughGateway = this.findByGatewayDefinition(gatewayDefinition);
                    if (!walkedThroughGateway.isPresent()) {
                        JoinGateway gw = new JoinGateway(seqDefs.size());
                        this.gateways.add(gw);
                        walkedThroughGateway = Optional.of(gw);
                    }
                    walkedThroughGateway.ifPresent(gw -> ((JoinGateway) gw).hit());
                default:
                    return;


            }
        });

    }

    protected void walkThrough(SequenceDefinition seq) {
        DefinitionType targetType = seq.getTargetType();
        switch (targetType) {
            case END:
                // TODO
                break;
            case TASK:
                this.definition.findTaskDefinition(seq.getTargetId()).ifPresent(taskDefinition -> {
                    this.tasks.add(Task.builder().definition(taskDefinition).build());
                });
                break;
            case GATEWAY:
                this.walkThrougnGateway(seq.getTargetId());
                break;
            default:
                // TODO  throw exeption

        }
    }

    private Optional<Gateway> findByGatewayDefinition(GatewayDefinition definition) {
        return this.gateways.stream().filter(gw -> gw.getDefinition().equals(definition)).findFirst();
    }


    @Override
    public void finish() {
        super.start();
    }

    public boolean isEnded() {
        return false;
    }

    public void startTask(String taskId) {
        this.definition.findTaskDefinition(taskId).ifPresent(taskDefinition -> {
            System.out.println("Starting Task: " + taskId);
        });
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // whether taskId is in current flow defintion
        if (!this.definition.hasTask(taskId)) {
            // TODO use exception
            return;
        }
        this.definition.findTaskDefinition(taskId).ifPresent(taskDefinition -> {
            // change task state according current task state and taskId
            System.out.println("Recording State of Task: " + taskId);

            System.out.println("Completed Task: " + taskId);

            List<SequenceDefinition> seqDefs = this.definition.findSequenceBySource(taskId);
            seqDefs.stream().forEach(this::walkThrough);

        });
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
