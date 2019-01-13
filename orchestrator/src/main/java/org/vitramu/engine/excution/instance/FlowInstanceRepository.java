package org.vitramu.engine.excution.instance;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FlowInstanceRepository {

    private FlowInstanceCache flowInstanceCache = new FlowInstanceCache();

    public Optional<FlowInstance> findById(String instanceId) {
        FlowInstance instance = new FlowInstance(instanceId);
        if (flowInstanceCache.exists(instanceId)) {
            return Optional.of(flowInstanceCache.restore(instance));
        }
        return Optional.ofNullable(instance);
    }


    public void save(FlowInstance instance) {
        flowInstanceCache.save(instance);
        // TODO save to storage
    }


}
