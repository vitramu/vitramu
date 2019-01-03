package org.vitramu.engine.excution.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.element.StartEvent;
import org.vitramu.engine.excution.instance.Flow;

/**
 * FlowService is used for rest controller or event listener and other application layer element to consume capability provided by Flow
 */
public interface FlowService {

    Flow startFlowInstance(String definitionId);

    void completeTask(String definitionId, String instanceId, String taskId);
}
