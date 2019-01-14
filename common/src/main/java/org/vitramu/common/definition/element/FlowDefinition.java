package org.vitramu.common.definition.element;

import lombok.*;
import org.vitramu.common.definition.AbstractDefinition;

public class FlowDefinition extends AbstractDefinition {

    @Builder
    public FlowDefinition(String id, String name) {
        super(id, name, DefinitionType.FLOW);
    }
}
