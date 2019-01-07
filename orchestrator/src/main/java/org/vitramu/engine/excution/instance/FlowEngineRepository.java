package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.vitramu.engine.definition.Definition;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@Repository
public class FlowEngineRepository {

    @Autowired
    private RedisStateMachinePersister<Definition, String> enginePersister;

    @Autowired
    public FlowEngineRepository(RedisStateMachinePersister enginePersister) {
        this.enginePersister = enginePersister;
    }

    public StateMachine<Definition, String> save(StateMachine<Definition, String> engine, String flowInstanceId) {
        saveInCache(engine, getContextKey(flowInstanceId));
        return engine;
    }

    public StateMachine<Definition, String> findByInstanceId(StateMachine<Definition, String> engine, String flowInstanceId) {
        StateMachine<Definition, String> resetEngine = loadFromCache(engine, flowInstanceId);
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

    private void saveInCache(StateMachine<Definition, String> engine, String flowInstanceId) {
        try {
            enginePersister.persist(engine, flowInstanceId);
        } catch (Exception e) {
            log.error("exception when saving engine", e);
        }
    }

    private StateMachine<Definition, String> loadFromCache(StateMachine<Definition, String> engine, String flowInstanceId) {
        // TODO check redis key
        StateMachine<Definition, String> restoredEngine = null;
        try {
            restoredEngine = enginePersister.restore(engine, getContextKey(flowInstanceId));
        } catch (Exception e) {
            log.error("exception when restoring engine", e);
        }
        return restoredEngine;
    }


}
