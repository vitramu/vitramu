package org.vitramu.engine.excution.instance;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;

@Slf4j
@Service
public class FlowInstanceService {

    private FlowEngineFactory engineFactory;
    private FlowInstanceRepository instanceRepository;
    private FlowDefinitionRepository definitionRepository;

    public FlowInstanceService(FlowEngineFactory engineFactory, FlowDefinitionRepository definitionRepository, FlowInstanceRepository flowInstanceRepository) {
        this.engineFactory = engineFactory;
        this.instanceRepository = flowInstanceRepository;
        this.definitionRepository = definitionRepository;
    }


    /**
     * flow对应的statemachine应该在应用启动后处于就绪状态，预定义的initial state是REQUEST_ARRIVED。
     * startFlowInstance向statemachine发送预定义的 INITIALIZED event，触发流程启动。
     */
    public FlowInstance getFlowInstance(final String definitionId, final String instanceId) {

        FlowDefinition definition = definitionRepository.findFlowDefinitionById(definitionId);
        FlowEngine engine = engineFactory.getFlowEngine(definitionId);
        FlowInstance instance = instanceRepository.findById(instanceId).orElse(
                new FlowInstance(instanceId)
        );
        instance.setDefinition(definition);
        instance.setEngine(engine);
        return instance;
    }

    public void pause(FlowInstance instance) {
        instanceRepository.save(instance);
        engineFactory.destory(instance.getEngine());
//        TODO local transaction finish
    }

    public void finish(FlowInstance instance) {
        instanceRepository.save(instance);
        engineFactory.destory(instance.getEngine());
//        TODO send reply
//        TODO do commit
    }

}
