package org.vitramu.engine.excution;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.element.DefinitionType;

public abstract class AbstractInstance<T extends Definition> implements Instance{


    @Setter(AccessLevel.PUBLIC)
    @Getter(AccessLevel.PROTECTED)
    protected T definition;

    protected AbstractInstance() {
        createdAt = System.currentTimeMillis();
    }

    @Override
    public String getDefinitionId() {
        return getDefinition().getId();
    }

    @Override
    public DefinitionType getDefinitionType() {
        return getDefinition().getType();
    }

    @Getter
    protected final Long createdAt;
}
