package org.vitramu.common.definition;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.element.FlowDefinition;


@Repository
public class FlowDefinitionRepository {
    @Autowired
    private StateRepository<MongoDbRepositoryState> stateRepository;

    @Autowired
    private TransitionRepository<MongoDbRepositoryTransition> transitionRepository;


    @Autowired
    private StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;

    public FlowDefinitionRepository() {
    }

    public FlowDefinition findFlowDefinitionById(String definitionId) {
        return FlowDefinition.builder().id(definitionId).build();
    }

    public FlowDefinition save(@NonNull FlowDefinition definition) {
        MongoDbRepositoryStateMachine sm = new MongoDbRepositoryStateMachine();
        sm.setMachineId(definition.getName());
        stateMachineRepository.save(sm);
        stateRepository.saveAll(definition.getStates());
        transitionRepository.saveAll(definition.getTransitions());
        return definition;
    }


}
