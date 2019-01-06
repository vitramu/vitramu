package org.vitramu.engine.excution.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.definition.element.TaskDefinition;

@Slf4j
public class FlowInstance {

    @Getter
    private final String instanceId;
    @Getter
    private final String parentInstanceId;
    private FlowDefinition definition;
    private StateMachine<TaskDefinition, String> engine;

    public FlowInstance(FlowDefinition definition, String instanceId, String parentInstanceId, StateMachine<TaskDefinition, String> engine) {
        this.definition = definition;
        this.engine = engine;
        this.instanceId = instanceId;
        this.parentInstanceId = parentInstanceId;
    }

    public void start() {
        engine.sendEvent("INITIALIZED");
    }

    public void pause() {
        try {
            FlowStateMachineFactory.persister.persist(engine, instanceId);
        } catch (Exception e) {
            log.error("pause exception", e);
        }
    }

    public void completeTask(String taskName, String taskInstanceIdId) {
        // TODO       find definitionId by instanceId
        //                restore statemachine specified by definitionId to state of instance specified by instanceId
        String event = buildTaskCompletionEvent(taskName);
        engine.sendEvent(event);
    }

    private String buildTaskCompletionEvent(String taskName) {
        // TODO
        return null;
    }
}
