package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.excution.instance.RedisStateMachineContextCache;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisStateMachineCacheTest {

    @Autowired
    RedisStateMachineContextCache cache;

    @Autowired
    StateMachineFactory<String, String> stateMachineFactory;

    @Test
    public void testHasKeyAfterSet() throws Exception {
        StateMachine<String, String> sm1 = stateMachineFactory.getStateMachine("SM-MongoDB-1");
        sm1.start();
        String key1 = "F1-1";
        StateMachinePersister<String, String, String> persister = new RedisStateMachinePersister<>(new RepositoryStateMachinePersist<>(cache));

        persister.persist(sm1, key1);

        assert cache.hasKey(key1);
    }
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    RedisTemplate<String, byte[]> redisTemplateBean;

    @Test
    public void testHasKeyAfterSetWithRestTemplate() throws Exception {
        StateMachine<String, String> sm1 = stateMachineFactory.getStateMachine("SM-MongoDB-1");
        sm1.start();
        String key1 = "F1-1";
        StateMachinePersister<String, String, String> persister = new RedisStateMachinePersister<>(new RepositoryStateMachinePersist<>(cache));
        persister.persist(sm1, key1);
        assert cache.hasKey(key1);

        RedisTemplate<String,Object> redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        assert redisTemplate.hasKey(key1);

        assert redisTemplateBean.hasKey(key1);
    }
}
