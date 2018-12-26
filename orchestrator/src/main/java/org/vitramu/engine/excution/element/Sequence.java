package org.vitramu.engine.excution.element;

import org.springframework.expression.Expression;
import org.vitramu.engine.definition.element.SequenceDefinition;
import org.vitramu.engine.excution.AbstractInstance;


public class Sequence extends AbstractInstance<SequenceDefinition>  {

    private Expression condition;

    public Sequence(SequenceDefinition definition) {
        this.definition = definition;
    }
}
