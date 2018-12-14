package org.vitramu.engine.definition;

import java.util.List;

public class FlowDefinition extends AbstractDefinition {

    private StartEventDefinition start;
    private EndEventDefinition end;
    private List<TaskDefinition> tasks;
    private List<SequenceDefinition> sequences;
    private List<GatewayDefinition> gateways;

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
}
