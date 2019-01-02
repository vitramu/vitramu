package org.vitramu.engine.excution.service;

import org.springframework.stereotype.Repository;
import org.vitramu.engine.excution.instance.Flow;

/**
 * FlowRepository provides capability like re-construct an existed flow instance from cache or database, persist flow instance state.
 * All cache and persistence related work should be done in this class.
 * */
@Repository
public class FlowRepository {

    public Flow findFlowInstanceById(String flowInstanceId) {
        // TODO
        return null;
    }

    public boolean isStarted(String flowInstanceId) {
        return false;
    }
}
