package org.vitramu.master.validator;

import org.vitramu.engine.definition.element.FlowDefinition;

@FunctionalInterface
public interface DefinitionConstraint {

    boolean isSatisfiedBy(FlowDefinition definition);
}
