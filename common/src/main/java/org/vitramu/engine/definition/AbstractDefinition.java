package org.vitramu.engine.definition;

import lombok.AllArgsConstructor;
import org.vitramu.engine.definition.element.DefinitionType;

@AllArgsConstructor
public abstract class AbstractDefinition implements Definition {
    protected final String id;
    protected final String name;
    protected final DefinitionType type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DefinitionType getType() {
        return this.type;
    }
}
