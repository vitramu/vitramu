package org.vitramu.engine.excution.instance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.excution.instance.statemachine.FlowEngineFactory;

/**
 * FlowInstanceFactory 提供根据flow definition id创建新的FlowInstance实例的能力。
 */
@Component
public class FlowInstanceFactory {


    private FlowDefinitionRepository definitionRepository;

    private FlowEngineFactory engineFactory;


    @Autowired
    public FlowInstanceFactory(FlowDefinitionRepository definitionRepository, FlowEngineFactory engineFactory) {
        this.definitionRepository = definitionRepository;
        this.engineFactory = engineFactory;
    }

    /**
     * 根据flowDefinitionId和flowInstanceId创建FlowInstance。
     * 需要处理flowDefinitionId对应的statemachine没有启动，没有定义，没有可用实例的各种异常。
     * @param flowDefinitionId
     * @param flowInstanceId
     * @return
     */
    public FlowInstance build(String flowDefinitionId, String flowInstanceId) {
        FlowDefinition definition = definitionRepository.findFlowDefinitionById(flowDefinitionId);
        StateMachine<Definition, String> engine = engineFactory.build(flowDefinitionId);
        return new FlowInstance(definition, engine, flowInstanceId);
    }

}
