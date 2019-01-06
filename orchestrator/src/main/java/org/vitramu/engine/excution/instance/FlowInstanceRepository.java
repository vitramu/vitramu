package org.vitramu.engine.excution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.instance.FlowInstance;

/**
 * FlowRepository provides capability like re-construct an existed flow instance from cache or database, persist flow instance state.
 * All cache and persistence related work should be done in this class.
 */
@Repository
public class FlowInstanceRepository {

    @Autowired
    private FlowDefinitionRepository flowDefinitionRepository;
    @Autowired
    private FlowStateMachineFactory flowFactory;

    public FlowInstance findFlowInstanceById(String flowInstanceId) {
        // TODO find definitionId by flowInstanceId
        String defId = findFlowDefinitionIdByInstanceId(flowInstanceId);
        return new FlowInstance(flowDefinitionRepository.findFlowDefinitionById(defId),
                flowInstanceId,
                findFlowParentInstanceIdByInstanceId(flowInstanceId),
                flowFactory.build(defId));
    }

    private String findFlowParentInstanceIdByInstanceId(String flowInstanceId) {
        return null;
    }

    private String findFlowDefinitionIdByInstanceId(String flowInstanceId) {
        return "PROTOTYPE";
    }

    public boolean isStarted(String flowInstanceId) {
        return false;
    }
}
