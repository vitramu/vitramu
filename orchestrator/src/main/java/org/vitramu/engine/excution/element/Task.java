package org.vitramu.engine.excution.element;

import lombok.Builder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.vitramu.engine.definition.element.DefinitionType;
import org.vitramu.engine.definition.element.TaskDefinition;
import org.vitramu.engine.excution.instance.AbstractExcutableInstance;
import org.vitramu.engine.excution.instance.TaskInstance;

import java.util.EnumSet;
import java.util.Set;

public class Task extends AbstractExcutableInstance<TaskDefinition> implements TaskInstance {

    private StateMachine<TaskState, TaskEvent> taskStateMachine;

    @Builder
    protected Task(TaskDefinition definition) {
        this.definition = definition;
        taskStateMachine = createTaskStateMachine();
        taskStateMachine.start();
    }

    @Override
    public String getDefinitionId() {
        return definition.getId();
    }

    @Override
    public DefinitionType getDefinitionType() {
        return definition.getType();
    }

    @Override
    public void start() {
        // persist task state
        // create and emit MQ event according task definition, flowInstanceId and input parameters
        System.out.println("Emitting message Task: " + this.getDefinitionId());
        this.on(TaskEvent.START_END);
    }

    private static StateMachine<TaskState, TaskEvent> createTaskStateMachine() {
        Set<TaskState> taskStates = EnumSet.allOf(TaskState.class);
        StateMachineBuilder.Builder<TaskState, TaskEvent> taskStateMachineBuilder = StateMachineBuilder.builder();
        try {
            taskStateMachineBuilder
                    .configureStates()
                    .withStates()
                    .initial(TaskState.INITIAL)
                    .states(taskStates);
            taskStateMachineBuilder.configureTransitions()
                    .withExternal().source(TaskState.INITIAL).target(TaskState.STARTED).event(TaskEvent.START_BEGIN)
                    .and()
                    .withExternal().source(TaskState.STARTED).target(TaskState.RUNNING).event(TaskEvent.START_END)
                    .and()
                    .withExternal().source(TaskState.RUNNING).target(TaskState.ABORTED).event(TaskEvent.ABORT)
                    .and()
                    .withExternal().source(TaskState.RUNNING).target(TaskState.COMPLETED).event(TaskEvent.COMPLETE)
                    .and()
                    .withExternal().source(TaskState.RUNNING).target(TaskState.ABORTED).event(TaskEvent.TIMEOUT)
                    .and()
                    .withExternal().source(TaskState.COMPLETED).target(TaskState.REVERTING).event(TaskEvent.ABORT)
                    .and()
                    .withExternal().source(TaskState.REVERTING).target(TaskState.FAILURE).event(TaskEvent.REVERT_FAILURE)
                    .and()
                    .withExternal().source(TaskState.REVERTING).target(TaskState.REVERTED).event(TaskEvent.REVERT_FINISH)
                    .and()
                    .withExternal().source(TaskState.INITIAL).target(TaskState.SKIPPED).event(TaskEvent.SKIP);
            return taskStateMachineBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void on(TaskEvent event) {
        taskStateMachine.sendEvent(event);
    }

    public enum TaskState {
        INITIAL,
        STARTED,
        RUNNING,
        ABORTED,
        COMPLETED,
        REVERTING,
        REVERTED,
        FAILURE,
        SKIPPED;
    }

    public enum TaskEvent {
        START_BEGIN,
        START_END,
        ABORT,
        COMPLETE,
        TIMEOUT,
        REVERT_FINISH,
        REVERT_FAILURE,
        SKIP;
    }
}
