package org.vitramu.engine.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.vitramu.engine.definition.element.DefinitionType;

@Getter
@AllArgsConstructor
public abstract class AbstractDefinition implements Definition {
    protected final String id;
    protected final String name;
    protected final DefinitionType type;

}
