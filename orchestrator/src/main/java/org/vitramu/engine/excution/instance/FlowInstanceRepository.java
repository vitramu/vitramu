package org.vitramu.engine.excution.instance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.instance.statemachine.FlowEngineFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FlowRepository provides capability like re-construct an existed flow instance from cache or database, persist flow instance state.
 * All cache and persistence related work should be done in this class.
 */
@Repository
public class FlowInstanceRepository implements CrudRepository<FlowInstance, String> {

    @Autowired
    private FlowDefinitionRepository flowDefinitionRepository;
    @Autowired
    private FlowEngineFactory flowFactory;

    private List<FlowInstance> repository = new ArrayList<>();

    private String findFlowParentInstanceIdByInstanceId(String flowInstanceId) {
        return null;
    }

    private String findFlowDefinitionIdByInstanceId(String flowInstanceId) {
        return "PROTOTYPE";
    }


    @Override
    public <S extends FlowInstance> S save(S entity) {
        repository.add(entity);
        return entity;
    }

    @Override
    public <S extends FlowInstance> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<FlowInstance> findById(String flowInstanceId) {
        return repository.stream().filter(instance -> instance.getInstanceId().equals(flowInstanceId)).findFirst();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<FlowInstance> findAll() {
        return null;
    }

    @Override
    public Iterable<FlowInstance> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(FlowInstance entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends FlowInstance> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
