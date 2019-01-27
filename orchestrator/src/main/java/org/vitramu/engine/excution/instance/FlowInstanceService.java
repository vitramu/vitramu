package org.vitramu.engine.excution.instance;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitramu.common.definition.FlowDefinitionRepository;
import org.vitramu.common.definition.element.FlowDefinition;

@Slf4j
@Service
public class FlowInstanceService {
    private FlowEngineCache engineCache;
    private FlowEngineFactory engineFactory;
    private FlowInstanceRepository instanceRepository;
    private FlowDefinitionRepository definitionRepository;

    @Autowired
    public FlowInstanceService(FlowEngineFactory engineFactory, FlowEngineCache engineCache, FlowDefinitionRepository definitionRepository, FlowInstanceRepository flowInstanceRepository) {
        this.engineFactory = engineFactory;
        this.engineCache = engineCache;
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
        if (engineCache.cached(instanceId)) {
            engineCache.restore(engine, instanceId);
        }
        instance.setEngine(engine);
        return instance;
    }

    public void pause(FlowInstance instance) {
        engineCache.save(instance.getEngine(), instance.getInstanceId());
        instanceRepository.save(instance);
        engineFactory.destory(instance.getEngine());
    }

    public void finish(FlowInstance instance) {
        instanceRepository.save(instance);
        engineFactory.destory(instance.getEngine());
//        TODO send reply
//        TODO do commit
    }

}
