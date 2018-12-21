package org.vitramu.engine.excution;

public interface FlowInstance extends ExcutableInstance {

    void completeTask(String taskId);

    void abortTask(String taskId);

    void abort();



}
