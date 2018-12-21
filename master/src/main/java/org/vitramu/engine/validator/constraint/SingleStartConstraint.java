package org.vitramu.engine.validator.constraint;

import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.validator.DefinitionConstraint;

public class SingleStartConstraint implements DefinitionConstraint {

    @Override
    public boolean isSatisfiedBy(FlowDefinition definition) {
        return false;
    }
}
