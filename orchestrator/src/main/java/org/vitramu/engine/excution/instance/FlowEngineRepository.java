package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@Repository
public class FlowEngineRepository {

    @Autowired
    private RedisStateMachinePersister<String, String> enginePersister;

    @Autowired
    public FlowEngineRepository(RedisStateMachinePersister enginePersister) {
        this.enginePersister = enginePersister;
    }

    public StateMachine<String, String> save(StateMachine<String, String> engine, String flowInstanceId) {
        saveInCache(engine, getContextKey(flowInstanceId));
        return engine;
    }

    public StateMachine<String, String> findByInstanceId(StateMachine<String, String> engine, String flowInstanceId) {
        StateMachine<String, String> resetEngine = loadFromCache(engine, flowInstanceId);
        if (null == resetEngine) {
            throw new NotImplementedException();
        }
        return resetEngine;
    }

    private static final String ENGINE_CONTEXT_NAMESPACE = "orchestrator:engine:context";
    private static final String DELIM = ":";

    private String getContextKey(String flowInstanceId) {
        return StringUtils.arrayToDelimitedString(new String[]{ENGINE_CONTEXT_NAMESPACE, flowInstanceId}, DELIM);
    }

    private void saveInCache(StateMachine<String, String> engine, String flowInstanceId) {
        try {
            enginePersister.persist(engine, flowInstanceId);
        } catch (Exception e) {
            log.error("exception when saving engine", e);
        }
    }

    private StateMachine<String, String> loadFromCache(StateMachine<String, String> engine, String flowInstanceId) {
        // TODO check redis key
        StateMachine<String, String> restoredEngine = null;
        try {
            restoredEngine = enginePersister.restore(engine, getContextKey(flowInstanceId));
        } catch (Exception e) {
            log.error("exception when restoring engine", e);
        }
        return restoredEngine;
    }


}
