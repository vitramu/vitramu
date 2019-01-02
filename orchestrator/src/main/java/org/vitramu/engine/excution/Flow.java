package org.vitramu.engine.excution;

import lombok.*;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.element.*;
import org.vitramu.engine.excution.element.*;

import java.util.*;

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

    private Deque<Definition> scheduled = new LinkedList<>();

    private List<JoinGateway> synchronizePoints = new ArrayList<>();

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
        enqueue(seqDef);
        schedule();
    }

    private boolean enqueue(Definition definition) {
        return scheduled.offer(definition);
    }

    private Definition dequeue() {
        return scheduled.poll();
    }

    private void schedule() {
        Definition def = dequeue();
        // empty queue, end current loop
        if (null == def) {
            return;
        }
        switch (def.getType()) {
            case SEQUENCE:
                this.visitSequence((SequenceDefinition) def);
                break;
            case TASK:
                this.startTask(def.getId());
                break;
            case GATEWAY_PARALLEL:
                this.visitParallelGateway((GatewayDefinition) def);
                break;
            case GATEWAY_EXCLUSIVE:
                this.visitExclusiveGateway((GatewayDefinition) def);
                break;
            case GATEWAY_INCLUSIVE:
                this.visitInclusiveGateway((GatewayDefinition) def);
                break;
            case GATEWAY_JOIN:
                this.visitJoinGateway((GatewayDefinition) def);
                break;
            case END:
                this.visitEnd((EndEventDefinition) def);
                break;
            default:
                System.out.println("can not schedule the type");
        }
        if (this.isEnded()) {
            System.out.println("the end");
        }
        schedule();
    }


    private void visitEnd(EndEventDefinition end) {
        System.out.println("flow arrivies at the end");
        // TODO make final response
    }

    private void visitSequence(@NonNull SequenceDefinition sequence) {
        // if sequence from exclusive or inclusive gateway
        // condition expression evaluates to true, trans along target
        // condition expression evaluates to false, skip
        System.out.println("visiting sequence: " + sequence.getId());

        switch (sequence.getTargetType()) {
            case TASK:
                definition.findTaskDefinition(sequence.getTargetId()).ifPresent(this::enqueue);
                break;
            case GATEWAY_PARALLEL:
            case GATEWAY_EXCLUSIVE:
            case GATEWAY_INCLUSIVE:
            case GATEWAY_JOIN:
                definition.findGatewayDefinition(sequence.getTargetId()).ifPresent(this::enqueue);
                break;
            case END:
                enqueue(definition.getEnd());
                break;
            default:
                System.out.println("not support the type");
        }
    }

    private void visitParallelGateway(@NonNull GatewayDefinition gateway) {
        System.out.println("visiting parallel gateway: " + gateway.getId());
        @NonNull List<SequenceDefinition> nextSeqDefs = this.definition.findSequenceBySource(gateway.getId());
        nextSeqDefs.stream().forEach(this::enqueue);
    }

    private void visitExclusiveGateway(@NonNull GatewayDefinition gateway) {

    }

    private void visitInclusiveGateway(@NonNull GatewayDefinition gateway) {

    }

    private void visitJoinGateway(@NonNull GatewayDefinition gateway) {
        System.out.println("visiting join gateway: " + gateway.getId());
        Optional<JoinGateway> gwOpt = synchronizePoints.stream()
                .filter(gw -> gw.getDefinitionId().equals(gateway.getId()))
                .findFirst();
        JoinGateway gw = gwOpt.orElseGet(() -> {
            System.out.println("creating new join gateway: " + gateway.getId());
            List<SequenceDefinition> seqDefs = definition.findSequenceByTarget(gateway.getId());
            JoinGateway jgw = new JoinGateway(seqDefs.size());
            jgw.setDefinition(gateway);
            synchronizePoints.add(jgw);
            return jgw;
        });
        gw.hit();

        if (!gw.isBlocked()) {
            @NonNull List<SequenceDefinition> seqDefs = definition.findSequenceBySource(gw.getDefinitionId());
            seqDefs.stream().forEach(this::enqueue);
        }
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
            Task taskInstance = Task.builder().definition(taskDefinition).build();
            taskInstance.on(Task.TaskEvent.START_BEGIN);

            taskInstance.start();
            this.tasks.add(taskInstance);
        });
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // whether taskId is in current flow defintion
//        if (!this.definition.hasTask(taskId)) {
//            // TODO use exception
//            return;
//        }
        Optional<Task> taskInstanceOpt = this.tasks.stream().filter(t -> t.getDefinitionId().equals(taskId)).findFirst();

        taskInstanceOpt.ifPresent(task -> {
            task.on(Task.TaskEvent.COMPLETE);
        });
        // change task state according current task state and taskId
        System.out.println("Recording State of Task: " + taskId);

        System.out.println("Completed Task: " + taskId);

        List<SequenceDefinition> seqDefs = this.definition.findSequenceBySource(taskId);
        seqDefs.stream().forEach(this::enqueue);
        schedule();
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
