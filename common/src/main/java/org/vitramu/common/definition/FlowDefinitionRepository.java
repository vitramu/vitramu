package org.vitramu.common.definition;

import lombok.var;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.element.FlowDefinition;

import java.util.ArrayList;


@Repository
public class FlowDefinitionRepository {
    private StateRepository<MongoDbRepositoryState> stateRepository;
    private TransitionRepository<MongoDbRepositoryTransition> transitionRepository;

    public FlowDefinition findFlowDefinitionById(String definitionId) {
        return FlowDefinition.builder().id(definitionId).build();
    }

    public FlowDefinition save(FlowDefinition definition) {

        var states = new ArrayList<MongoDbRepositoryState>();
        for (StateData<String, String> sd : definition.getStates()) {
            MongoDbRepositoryState state = new MongoDbRepositoryState();
            state.setMachineId(sd.get);
        }
        return definition;
    }
}
