package org.vitramu.engine.excution.instance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Repository;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FlowRepository provides capability like re-construct an existed flow instance from cache or database, persist flow instance state.
 * All cache and persistence related work should be done in this class.
 */
@Repository
public class FlowInstanceRepository {

    @Autowired
    private FlowDefinitionRepository flowDefinitionRepository;
    @Autowired
    private FlowEngineRepository flowEngineRepository;

    @Autowired
    private FlowEngineBuilder flowEngineBuilder;

    private List<FlowInstance> repository = new ArrayList<>();

    public FlowInstance save(FlowInstance entity) {
        flowEngineRepository.save(entity.getEngine(), entity.getInstanceId());
        repository.add(entity);
        return entity;
    }

    public <S extends FlowInstance> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    public Optional<FlowInstance> findById(final String flowInstanceId) {
        Optional<FlowInstance> storedInstaceOpt = repository.stream().filter(instance -> instance.getInstanceId().equals(flowInstanceId)).findFirst();
        //        TODO dump implentation
        storedInstaceOpt.ifPresent(instance -> repository.remove(instance));
        return storedInstaceOpt.map(instance -> {
            FlowDefinition definition = instance.getDefinition();
            StateMachine<String, String> engine = flowEngineBuilder.build(instance.getDefinition());
            flowEngineRepository.findByInstanceId(engine, flowInstanceId);
            return new FlowInstance(definition, engine, flowInstanceId, instance.getParentInstanceId());
        });
    }

}
