package org.vitramu.engine.definition;

public abstract class AbstractDefinition implements Definition {
    protected String id;
    protected String name;
    protected Type type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }
}
