package org.vitramu.engine.excution.instance;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vitramu.common.definition.element.FlowDefinition;

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
    @Setter
    private FlowDefinition definition;

    @Getter
    @Setter
    private FlowEngine engine;

    public FlowInstance(@NonNull String instanceId) {
        this.instanceId = instanceId;
    }

    public FlowInstance(String instanceId, @NonNull FlowDefinition definition, @NonNull FlowEngine engine) {
        this.instanceId = instanceId;
        this.definition = definition;
        this.engine = engine;
    }

    public boolean isEnded() {
        return false;
    }

    public void start() {
        engine.sendEvent("INITIALIZED");
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
