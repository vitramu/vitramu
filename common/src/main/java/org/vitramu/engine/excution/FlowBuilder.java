package org.vitramu.engine.excution;

import org.vitramu.engine.definition.FlowDefinitionRepository;

public class FlowBuilder {

    private FlowDefinitionRepository flowDefinitionRepository;

    private String flowInstanceId;

    private String flowDefinitionId;

    public FlowBuilder(FlowDefinitionRepository flowDefinitionRepository) {
        this.flowDefinitionRepository = flowDefinitionRepository;
    }

    public FlowBuilder flowInstanceId(final String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
        return this;
    }

    public FlowBuilder flowDefinitionId(final String flowDefinitionId) {
        this.flowDefinitionId = flowDefinitionId;
        return this;
    }

    public Flow build() {
        assert null != flowInstanceId || null != flowDefinitionId;
        Flow flowInstance =  new Flow(flowInstanceId, flowDefinitionRepository.findFlowDefinitionById(flowDefinitionId));
        // TODO
        return flowInstance;
    }

}
