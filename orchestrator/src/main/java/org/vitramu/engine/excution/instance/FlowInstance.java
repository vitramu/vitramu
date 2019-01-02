package org.vitramu.engine.excution.instance;

public interface FlowInstance extends ExcutableInstance {

    void completeTask(String taskId);

    void abortTask(String taskId);

    void abort();



}
