package org.vitramu.engine.excution.instance;

public interface ExcutableInstance extends Instance {

    Long getStartedAt();

    Long getFinishedAt();

    void start();

    void finish();
}
