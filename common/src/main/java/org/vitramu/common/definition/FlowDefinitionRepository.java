package org.vitramu.common.definition;

import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.element.FlowDefinition;


@Repository
public class FlowDefinitionRepository {

    public FlowDefinition findFlowDefinitionById(String definitionId) {
        return FlowDefinition.builder().id(definitionId).build();
    }
}
