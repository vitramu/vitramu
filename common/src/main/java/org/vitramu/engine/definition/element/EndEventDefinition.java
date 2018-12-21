package org.vitramu.engine.definition.element;

import org.vitramu.engine.definition.AbstractDefinition;

public class EndEventDefinition extends AbstractDefinition {

    private String schema; // TODO use Typed Json Object

    public EndEventDefinition(String id, String name) {
        super(id, name, DefinitionType.END);
    }
}
