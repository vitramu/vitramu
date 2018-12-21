package org.vitramu.engine.excution;

import org.vitramu.engine.definition.element.DefinitionType;

public interface Instance {

    String getDefinitionId();

    DefinitionType getDefinitionType();

    Long getCreatedAt();

}
