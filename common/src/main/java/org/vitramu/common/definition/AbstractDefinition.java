package org.vitramu.common.definition;

import lombok.*;
import org.vitramu.common.definition.element.DefinitionType;


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
