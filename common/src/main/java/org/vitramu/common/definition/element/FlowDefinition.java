package org.vitramu.common.definition.element;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.config.model.TransitionData;
import org.vitramu.common.definition.AbstractDefinition;
import org.vitramu.common.definition.FlowDefinitionXmlDocument;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Document(collection = FlowDefinition.DEFINITION_COLLECTION_NAME)
@ToString
@Getter
public class FlowDefinition extends AbstractDefinition {
    public static final String DEFINITION_COLLECTION_NAME = "definition";

    @Getter
    private StateMachineModel<String,String> model;
    @Builder
    public FlowDefinition(String id, String name) {
        super(id, name, DefinitionType.FLOW);
    }

    public void setModel(@NonNull StateMachineModel<String,String> model) {
        this.model = model;
    }

    public Iterable<StateData<String,String>> getStates() {
        return model.getStatesData().getStateData();
    }

    public Iterable<TransitionData<String,String>> getTransitions() {
        return model.getTransitionsData().getTransitions();
    }



}
