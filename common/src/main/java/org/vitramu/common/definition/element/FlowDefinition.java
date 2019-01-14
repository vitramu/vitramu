package org.vitramu.common.definition.element;

import lombok.*;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.config.model.TransitionData;
import org.vitramu.common.definition.AbstractDefinition;

public class FlowDefinition extends AbstractDefinition {

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
