package org.vitramu.common.definition;

import com.mongodb.MongoClient;
import lombok.var;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.element.FlowDefinition;

import java.util.ArrayList;

import static org.vitramu.common.definition.FlowDefinitionXmlDocumentRepository.FLOW_DEFINITION_DB_NAME;


@Repository
public class FlowDefinitionRepository {
    private StateRepository<MongoDbRepositoryState> stateRepository;
    private TransitionRepository<MongoDbRepositoryTransition> transitionRepository;

    private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(), FLOW_DEFINITION_DB_NAME);
    public FlowDefinitionRepository() {

    }
    public FlowDefinition findFlowDefinitionById(String definitionId) {
        return FlowDefinition.builder().id(definitionId).build();
    }

    public FlowDefinition save(FlowDefinition definition) {
        mongoTemplate.save(definition);
        return definition;
    }
}
