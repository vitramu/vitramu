package org.vitramu.engine.excution.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.RepositoryStateMachineModelFactory;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;

@Configuration
@EnableStateMachineFactory
public class MongoStateMachineFactoryConfig extends StateMachineConfigurerAdapter<String, String> {
    @Autowired
    private StateRepository<MongoDbRepositoryState> stateRepository;
    @Autowired
    private TransitionRepository<MongoDbRepositoryTransition> transitionRepository;

    @Override
    public void configure(StateMachineModelConfigurer<String, String> configurer) throws Exception {
        configurer.withModel().factory(modelFactory());
    }

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
    }
}
