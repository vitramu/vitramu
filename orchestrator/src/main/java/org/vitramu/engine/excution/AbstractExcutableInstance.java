package org.vitramu.engine.excution;

import lombok.Getter;
import org.vitramu.engine.definition.Definition;

public abstract class AbstractExcutableInstance<T extends Definition> extends AbstractInstance<T> implements ExcutableInstance {

    @Getter
    protected Long startedAt;
    @Getter
    protected Long finishedAt;

    public void start() {
        this.startedAt = System.currentTimeMillis();
    }


    public void finish() {
        this.finishedAt = System.currentTimeMillis();
    }

}
