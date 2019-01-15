package org.vitramu.common.definition.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class FlowDefinitionModel {
    private StateMachineModel<String, String> model;

    private String machineId;
    private Collection<MongoDbRepositoryState> states;

    private Collection<MongoDbRepositoryTransition> transitions;

    public FlowDefinitionModel(String definitionId, StateMachineModel<String, String> model) {
        this.model = model;
        machineId = definitionId;
        states = model.getStatesData().getStateData().stream().map(s -> {
            MongoDbRepositoryState state = new MongoDbRepositoryState(
                    machineId,
                    null,
                    s.getState(),
                    s.isInitial()
            );
            state.setKind(s.getPseudoStateKind());
            state.setRegion((String)s.getRegion());
            return state;
        }).collect(Collectors.toList());
        Map<String, MongoDbRepositoryState> statesMap = states.stream().collect(HashMap::new, (map, state) -> map.put(state.getState(), state), HashMap::putAll);

        model.getStatesData().getStateData().stream().forEach(s -> Optional.ofNullable((String) s.getParent())
                .ifPresent(
                        p -> statesMap.get(s.getState()).setParentState(statesMap.get(p))
                ));

        transitions = model.getTransitionsData().getTransitions().stream().map(t -> new MongoDbRepositoryTransition(
                machineId,
                statesMap.get(t.getSource()),
                statesMap.get(t.getTarget()),
                t.getEvent()
        )).collect(Collectors.toList());
    }

    public Collection<MongoDbRepositoryState> getStates() {
        return states;
    }

    public Collection<MongoDbRepositoryTransition> getTransitions() {
        return transitions;
    }
}
