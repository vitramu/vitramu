package org.vitramu.engine.excution.element;

import org.vitramu.engine.definition.element.DefinitionType;

public class ParallelGateway extends Gateway {
    @Override
    public boolean shouldWait() {
        return false;
    }

}
