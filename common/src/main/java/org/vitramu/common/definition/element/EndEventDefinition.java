package org.vitramu.common.definition.element;

import org.vitramu.common.definition.AbstractDefinition;

public class EndEventDefinition extends AbstractDefinition {

    private String schema; // TODO use Typed Json Object

    public EndEventDefinition(String id, String name) {
        super(id, name, DefinitionType.END);
    }
}
