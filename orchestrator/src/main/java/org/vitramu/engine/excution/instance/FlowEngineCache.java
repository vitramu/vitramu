package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlowEngineCache {
    public static final String INSTANCE_NAMESPACE = "ENGINE:CONTEXT:";

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final RedisStateMachineContextCache stateMachineContextCache;
    private final RedisStateMachinePersister<String, String> stateMachinePersister;

    public FlowEngineCache(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stateMachineContextCache = new RedisStateMachineContextCache(redisTemplate);
        this.stateMachinePersister = new RedisStateMachinePersister<>(this.stateMachineContextCache);
    }

    public FlowEngine restore(FlowEngine engine, String instanceId) {
        if (instanceId == null) {
            return engine;
        }
        try {
            stateMachinePersister.restore(engine.getStateMachine(), buildInstanceContextKey(instanceId));
        } catch (Exception e) {
            log.error("Restroing flow engine with definitionId={} and instanceId={} failed", engine.getDefinitionId(), instanceId);
        }
        return engine;
    }

    public void save(FlowEngine engine, String instanceId) {
        log.info("Caching instance with definitionId={} and instanceId={}", engine.getDefinitionId(), instanceId);
        try {
            stateMachinePersister.persist(engine.getStateMachine(), buildInstanceContextKey(instanceId));
        } catch (Exception e) {
            log.error("Caching flow engine with definitionId={} and instanceId={} failed", engine.getDefinitionId(), instanceId);
        }
    }

    public static String buildInstanceContextKey(String instanceId) {
        return INSTANCE_NAMESPACE + instanceId;
    }
}
