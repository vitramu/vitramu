package org.vitramu.engine.validator;

import org.vitramu.engine.definition.element.FlowDefinition;

import java.util.List;

public class FlowExcutionValidator implements Validator<FlowDefinition> {

    private List<DefinitionConstraint> constraints;

    @Override
    public boolean validate(FlowDefinition definition) {
        return false;
    }
}
