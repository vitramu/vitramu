package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.excution.service.FlowEngineService;

/**
 * FlowInstanceFactory 提供根据flow definition id创建新的FlowInstance实例的能力。
 */
@Slf4j
@Component
public class FlowInstanceFactory {

    private FlowDefinitionRepository definitionRepository;

    private FlowEngineService engineService;


    @Autowired
    public FlowInstanceFactory(FlowDefinitionRepository definitionRepository, FlowEngineService engineService) {
        this.definitionRepository = definitionRepository;
        this.engineService = engineService;
    }

    /**
     * 根据flowDefinitionId和flowInstanceId创建FlowInstance。
     * 需要处理flowDefinitionId对应的statemachine没有启动，没有定义，没有可用实例的各种异常。
     *
     * @param flowDefinitionId
     * @param flowInstanceId
     * @return
     */
    public FlowInstance build(String flowDefinitionId, String flowInstanceId) {
        FlowDefinition definition = definitionRepository.findFlowDefinitionById(flowDefinitionId);
        StateMachine<String, String> engine = null;
        try {
            engine = engineService.acquireEnginePrototype(flowDefinitionId);
        } catch (Exception e) {
            log.error("Build flow engine failed with flowDefinitionId={} and flowInstanceId={}", flowDefinitionId, flowInstanceId, e);
        }
        return new FlowInstance(definition, engine, flowInstanceId);
    }

}
