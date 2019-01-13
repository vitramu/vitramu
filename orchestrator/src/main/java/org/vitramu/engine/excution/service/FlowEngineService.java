package org.vitramu.engine.excution.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.access.StateMachineFunction;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Service;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;
import org.vitramu.engine.excution.instance.RedisStateMachineContextCache;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FlowEngineService {

    public static final String PROTOTYPE_NAMESPACE = "ENGINE:CONTEXT:PROTOTYPE:";
    public static final String INSTANCE_NAMESPACE = "ENGINE:CONTEXT:INSTANCE:";

    private final Map<String, StateMachine<String, String>> engines = new HashMap<>();
    private final RedisStateMachineContextCache stateMachineContextCache;
    @Setter
    private StateMachineFactory<String, String> stateMachineFactory;

    @Setter
    private StateMachinePersist<String, String, String> stateMachinePersist;
    private RedisStateMachinePersister<String, String> stateMachinePersister;

    public FlowEngineService(StateMachineFactory<String, String> stateMachineFactory, RedisStateMachineContextCache contextCache) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateMachineContextCache = contextCache;
        this.stateMachinePersister = new RedisStateMachinePersister<>(contextCache);
    }

    public static String buildPrototypeContextKey(String definitionId) {
        return PROTOTYPE_NAMESPACE + definitionId;
    }

    public static String buildInstanceContextKey(String instanceId) {
        return INSTANCE_NAMESPACE + instanceId;
    }

    /**
     * @param definitionId
     * @param
     * @return
     * @throws Exception
     */
    public StateMachine<String, String> acquireEnginePrototype(String definitionId) throws Exception {
        log.info("Acquiring machine with id " + definitionId);
        StateMachine<String, String> engine;
        synchronized (engines) {
            engine = engines.get(definitionId);
            if (engine == null) {
                log.info("Getting new machine from factory with id " + definitionId);
                engine = stateMachineFactory.getStateMachine(definitionId);
//                TODO dump implementation
                engine.addStateListener(new FlowEngineEventListener());
//               must start, otherwise NPE
                engine.start();
                if (!isPrototypeCached(definitionId)) {
                    cachePrototype(engine);
                }
                engines.put(definitionId, engine);
            } else {
                restoreStateMachine(engine);
            }
        }
        return engine;
    }

    protected boolean isInstanceCached(String instanceId) {
        return stateMachineContextCache.hasKey(buildInstanceContextKey(instanceId));
    }

    protected boolean isPrototypeCached(String definitionId) {
        return stateMachineContextCache.hasKey(buildPrototypeContextKey(definitionId));
    }

    protected synchronized void cachePrototype(StateMachine<String, String> prototype) throws Exception {
        log.info("Caching prototype with id={}", prototype.getId());
        stateMachinePersister.persist(prototype, buildPrototypeContextKey(prototype.getId()));
    }

    public synchronized void cacheInstance(StateMachine<String, String> engine, String instanceId) throws Exception {
        log.info("Caching instance with definitionId={} and instanceId={}", engine.getId(), instanceId);
        stateMachinePersister.persist(engine, buildInstanceContextKey(instanceId));
    }

    public StateMachine<String, String> restoreStateMachine(StateMachine<String, String> stateMachine, String instanceId) throws Exception {
        if (instanceId == null) {
            return stateMachine;
        }
        stateMachinePersister.restore(stateMachine, buildInstanceContextKey(instanceId));
        return stateMachine;
    }

    protected StateMachine<String, String> restoreStateMachine(StateMachine<String, String> stateMachine) throws Exception {
        if (isPrototypeCached(stateMachine.getId())) {
            log.info("Restore engine prototype from cache");
            stateMachinePersister.restore(stateMachine, buildPrototypeContextKey(stateMachine.getId()));
        } else {
            log.info("Engine prototype cache not found, restart engine");
            stateMachine.stop();
            stateMachine.start();
        }
        return stateMachine;
    }

    /**
     * 释放引擎资源
     *
     * @param definitionId
     */
    public void releaseStateMachine(String definitionId) {
        log.info("Releasing machine with id " + definitionId);
        synchronized (engines) {
            StateMachine<String, String> engine = engines.remove(definitionId);
            if (engine != null) {
                log.info("Found machine with id " + definitionId);
                engine.stop();
            }
        }
    }
}
