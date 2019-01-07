package org.vitramu.engine.excution.instance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;

/**
 * FlowInstanceFactory 提供根据flow definition id创建新的FlowInstance实例的能力。
 */
@Component
public class FlowInstanceFactory {


    private FlowDefinitionRepository definitionRepository;

    private FlowEngineBuilder engineBuilder;


    @Autowired
    public FlowInstanceFactory(FlowDefinitionRepository definitionRepository, FlowEngineBuilder engineBuilder) {
        this.definitionRepository = definitionRepository;
        this.engineBuilder = engineBuilder;
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
        StateMachine<Definition, String> engine = engineBuilder.build(definition);
        return new FlowInstance(definition, engine, flowInstanceId);
    }

}
