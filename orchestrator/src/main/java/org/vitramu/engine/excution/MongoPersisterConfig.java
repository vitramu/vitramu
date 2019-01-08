package org.vitramu.engine.excution;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.vitramu.engine.definition.Definition;

@Profile("mongo")
@Configuration
public class MongoPersisterConfig {

    @Bean
    public StateMachineRuntimePersister<Definition, String, String> stateMachineRuntimePersister(
            MongoDbStateMachineRepository stateMachineRepository) {
        return new MongoDbPersistingStateMachineInterceptor<>(stateMachineRepository);
    }
}
