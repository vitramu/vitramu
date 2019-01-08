package org.vitramu.engine.excution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

@SpringBootApplication(scanBasePackages = {"org.vitramu.engine"})
public class OrchestratorApplication {


    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }


    @Bean
    public StateMachinePersist<String, String, String> stateMachinePersist(RedisConnectionFactory connectionFactory) {
        RedisStateMachineContextRepository<String, String> repository =
                new RedisStateMachineContextRepository<>(connectionFactory);
        return new RepositoryStateMachinePersist<>(repository);
    }

    @Bean
    public RedisStateMachinePersister<String , String> redisStateMachinePersister(
            StateMachinePersist<String, String, String> stateMachinePersist) {
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }


}
