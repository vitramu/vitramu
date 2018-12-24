package org.vitramu.engine.definition.element;

import lombok.*;
import org.vitramu.engine.definition.AbstractDefinition;

import java.util.*;
import java.util.stream.Collectors;

@Builder
public class FlowDefinition extends AbstractDefinition {

    @Setter
    private StartEventDefinition start;
    @Setter
    private EndEventDefinition end;
    @Singular
    private List<TaskDefinition> tasks = new ArrayList<>();
    @Singular
    private List<SequenceDefinition> sequences = new ArrayList<>();
    @Singular
    private List<GatewayDefinition> gateways = new ArrayList<>();


    public FlowDefinition(@NonNull String id, @NonNull String name) {
        super(id, name, DefinitionType.FLOW);
    }

    public FlowDefinition(@NonNull String id, @NonNull String name, StartEventDefinition start, EndEventDefinition end, Collection<TaskDefinition> taskDefinitions, Collection<GatewayDefinition> gatewayDefinitions) {
        super(id, name, DefinitionType.FLOW);
        this.start = start;
        this.end = end;
        tasks.addAll(taskDefinitions);
        gateways.addAll(gatewayDefinitions);
    }

    public Optional<TaskDefinition> findTaskDefinition(String taskId) {
        return this.tasks.stream().filter(td->td.getId().equals(taskId)).findFirst();
    }

    public Optional<GatewayDefinition> findGatewayDefinition(String gatewayId) {
        return this.gateways.stream().filter(gw->gw.getId().equals(gatewayId)).findFirst();
    }
    public SequenceDefinition findSequenceByStart() {
        // TODO
        List<SequenceDefinition> seqList = sequences.stream().filter(seq -> seq.getSourceId().equals(start.getId())).collect(Collectors.toList());
        if (seqList.size() > 0) {
            return seqList.get(0);
        }
        // TODO use exception
        return null;
    }

    public List<SequenceDefinition> findSequenceBySource(@NonNull String elementId) {
        return this.sequences.stream().filter(seq -> seq.getSourceId().equals(elementId)).collect(Collectors.toList());
    }

    public List<SequenceDefinition> findSequenceBySource(TaskDefinition task) {
        return this.sequences.stream().filter(seq -> seq.getSourceId().equals(task.getId())).collect(Collectors.toList());
    }

    public List<SequenceDefinition> findSequenceByTarget(@NonNull String elementId) {
        return sequences.stream().filter(seq -> seq.getTargetId().equals(elementId)).collect(Collectors.toList());
    }

    public List<SequenceDefinition> findSequenceByTarget(TaskDefinition task) {
        // TODO
        return null;
    }

    public List<SequenceDefinition> findSequenceByTarget(GatewayDefinition gateway) {
        // TODO
        return null;
    }

    public boolean hasTask(String taskId) {
        return tasks.stream().map(TaskDefinition::getId).collect(Collectors.toSet()).contains(taskId);
    }

    public FlowDefinition sequence(SequenceDefinition sequence) {
        this.sequences.add(sequence);
        return this;
    }

    public FlowDefinition sequences(Collection<SequenceDefinition> sequenceCollection) {
        this.sequences.addAll(sequenceCollection);
        return this;
    }

    public FlowDefinition connect(@NonNull String sequenceId, @NonNull String sourceId, @NonNull String targetId) {
        // TODO whether sequence already exist

        Map<String, DefinitionType> elementTypeMap = new HashMap<>();
        elementTypeMap.putAll(
                tasks.stream().collect(Collectors.toMap(TaskDefinition::getId, TaskDefinition::getType))
        );
        elementTypeMap.putAll(
                gateways.stream().collect(Collectors.toMap(GatewayDefinition::getId, GatewayDefinition::getType))
        );

        DefinitionType sourceType = null;
        if (start.getId().equals(sourceId)) {
            sourceType = start.getType();
        } else {
            sourceType = elementTypeMap.get(sourceId);
        }

        DefinitionType targetType = null;
        if (end.getId().equals(targetId)) {
            targetType = end.getType();
        } else {
            targetType = elementTypeMap.get(targetId);
        }

        this.sequence(new SequenceDefinition(sequenceId, sequenceId)
                .source(sourceId, sourceType)
                .target(targetId, targetType));
        return this;
    }

    public static class FlowDefinitionBuilder {

        private String id;
        private String name;

        public FlowDefinitionBuilder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public FlowDefinitionBuilder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public FlowDefinition build() {
            FlowDefinition flowDefinition = new FlowDefinition(id, name, start, end, tasks, gateways);
            return flowDefinition;
        }
    }

}
