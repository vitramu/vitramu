package org.vitramu.engine.excution.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.LifecycleObjectSupport;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.element.FlowDefinition;

/**
 * 封装flow definition和flow engine。当前engine是使用statemahcine实现的。
 * FlowInstance需要屏蔽statemachine具体实现细节，为后面优化engine、替换engine做好铺垫。
 */
@Slf4j
public class FlowInstance {

    @Getter
    private final String instanceId;
    @Getter
    @Setter
    private String parentInstanceId;

    @Getter
    @Setter
    private String startServiceName;
    @Getter
    @Setter
    private String startServiceInstanceId;

    @Getter
    private FlowDefinition definition;

    @Getter
    private StateMachine<Definition, String> engine;

    public FlowInstance(FlowDefinition definition, StateMachine<Definition, String> engine, String instanceId, String parentInstanceId) {
        this.definition = definition;
        this.engine = engine;
        this.instanceId = instanceId;
        this.parentInstanceId = parentInstanceId;
    }

    public FlowInstance(FlowDefinition definition, StateMachine<Definition, String> engine, String instanceId) {
        this.instanceId = instanceId;
        this.definition = definition;
        this.engine = engine;
    }

    public boolean isRunning() {
        // TODO this is hack code, try to refactor
        return ((LifecycleObjectSupport) engine).isRunning();
    }

    public void start() {
        engine.sendEvent("INITIALIZED");
    }

    public void stop() {
        engine.stop();
    }

    public void completeTask(String taskInstanceId, String event) {
        // TODO       find definitionId by instanceId
        //                restore statemachine specified by definitionId to state of instance specified by instanceId
        engine.sendEvent(event);
    }

    public void abortTask(String taskInstanceId, String event) {
        // if no customized error handling, abort the whole flow instance
        //
        engine.sendEvent(event);
    }

}
