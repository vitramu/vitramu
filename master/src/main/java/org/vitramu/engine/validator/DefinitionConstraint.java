package org.vitramu.engine.validator;

import org.vitramu.engine.definition.element.FlowDefinition;

@FunctionalInterface
public interface DefinitionConstraint {

    boolean isSatisfiedBy(FlowDefinition definition);
}
