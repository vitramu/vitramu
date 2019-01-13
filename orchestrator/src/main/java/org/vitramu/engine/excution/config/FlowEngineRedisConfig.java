package org.vitramu.engine.excution.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.vitramu.engine.excution.instance.RedisStateMachineContextCache;

@Configuration
public class FlowEngineRedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean("contextTemplate")
    public RedisTemplate<String, byte[]> contextTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisStateMachineContextCache contextRepository(RedisTemplate<String, byte[]> contextTemplate) {
        RedisStateMachineContextCache repository =
                new RedisStateMachineContextCache(contextTemplate);
        return repository;
    }

//    @Bean
//    public StateMachinePersist<String, String, String> stateMachinePersist(RedisStateMachineContextCache repository) {
//        return new RepositoryStateMachinePersist<>(repository);
//    }

//    @Bean
//    public RedisStateMachinePersister<String, String> redisStateMachinePersister(
//            StateMachinePersist<String, String, String> stateMachinePersist) {
//        return new RedisStateMachinePersister<>(stateMachinePersist);
//    }
}
