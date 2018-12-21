package org.vitramu.engine.definition.element;

import org.vitramu.engine.definition.AbstractDefinition;

public class StartEventDefinition  extends AbstractDefinition {

    private String schema; // TODO use Typed Json Object

    public StartEventDefinition(String id, String name) {
        super(id,name,DefinitionType.START);
    }

}
