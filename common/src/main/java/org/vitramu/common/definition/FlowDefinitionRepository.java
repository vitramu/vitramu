package org.vitramu.common.definition;

import com.mongodb.MongoClient;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.adapter.FlowDefinitionModel;
import org.vitramu.common.definition.element.FlowDefinition;

import static org.vitramu.common.definition.FlowDefinitionXmlDocumentRepository.FLOW_DEFINITION_DB_NAME;


@Repository
public class FlowDefinitionRepository {
    @Autowired
    private StateRepository<MongoDbRepositoryState> stateRepository;

    @Autowired
    private TransitionRepository<MongoDbRepositoryTransition> transitionRepository;

    private MongoTemplate mongoTemplate;
    public FlowDefinitionRepository(MongoClient mongo) {
        mongoTemplate = new MongoTemplate(mongo, FLOW_DEFINITION_DB_NAME);
    }

    public FlowDefinition findFlowDefinitionById(String definitionId) {
        return FlowDefinition.builder().id(definitionId).build();
    }

    public FlowDefinition save(@NonNull FlowDefinition definition) {
        FlowDefinitionModel defModel = new FlowDefinitionModel(definition.getName(), definition.getModel());
        stateRepository.saveAll(defModel.getStates());
        transitionRepository.saveAll(defModel.getTransitions());
        return definition;
    }


}
