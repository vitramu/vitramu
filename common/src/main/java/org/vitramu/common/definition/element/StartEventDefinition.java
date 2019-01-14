package org.vitramu.common.definition.element;

import org.vitramu.common.definition.AbstractDefinition;

public class StartEventDefinition  extends AbstractDefinition {

    private String schema; // TODO use Typed Json Object

    public StartEventDefinition(String id, String name) {
        super(id,name,DefinitionType.START);
    }

}
