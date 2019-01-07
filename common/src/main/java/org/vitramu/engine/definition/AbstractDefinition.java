package org.vitramu.engine.definition;

import lombok.*;
import org.vitramu.engine.definition.element.DefinitionType;


@Setter
@Getter
@ToString
@AllArgsConstructor
public abstract class AbstractDefinition implements Definition {
    protected String id;
    protected String name;
    protected DefinitionType type;

    public AbstractDefinition() {
    }
}
