package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@ToString
public class FlowDefinitionModel {
    private String machineId;
    @Getter
    private Collection<MongoDbRepositoryState> states;

    @Getter
    private Collection<MongoDbRepositoryTransition> transitions;

    public FlowDefinitionModel(String definitionId, Model model) {
        machineId = definitionId;
        Region region = model.getPackagedElement().getRegion();
        Collection<Subvertex> subvertices = new ArrayList<>();
        Collection<Transition> transitions = new ArrayList<>();
    }



}
