package org.vitramu.engine.definition.element;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import org.vitramu.engine.definition.AbstractDefinition;

import java.util.List;
import java.util.stream.Collectors;

public class FlowDefinition extends AbstractDefinition {

    private StartEventDefinition start;
    private EndEventDefinition end;
    @Singular
    private List<TaskDefinition> tasks;
    @Singular
    private List<SequenceDefinition> sequences;
    @Singular
    private List<GatewayDefinition> gateways;

    @Builder
    public FlowDefinition(@NonNull String id, @NonNull String name) {
        super(id,name,DefinitionType.FLOW);
    }

    public SequenceDefinition findSequenceBySource(StartEventDefinition start) {
        // TODO
        return null;
    }
    public List<SequenceDefinition> findSequenceBySource(TaskDefinition task) {
        // TODO
        return null;
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


}
