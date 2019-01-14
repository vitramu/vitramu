package org.vitramu.master.validator.constraint;

import org.vitramu.common.definition.element.FlowDefinition;
import org.vitramu.master.validator.DefinitionConstraint;

public class SingleStartConstraint implements DefinitionConstraint {

    @Override
    public boolean isSatisfiedBy(FlowDefinition definition) {
        return false;
    }
}
