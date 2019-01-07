package org.vitramu.engine.definition.element;

import lombok.*;
import org.vitramu.engine.definition.AbstractDefinition;

import java.util.*;
import java.util.stream.Collectors;

public class FlowDefinition extends AbstractDefinition {

    @Builder
    public FlowDefinition(String id, String name) {
        super(id, name, DefinitionType.FLOW);
    }
}
