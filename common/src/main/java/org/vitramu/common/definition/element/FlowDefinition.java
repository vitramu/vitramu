package org.vitramu.common.definition.element;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.vitramu.common.definition.AbstractDefinition;
import org.vitramu.common.definition.xml.SubvertexToStateMapper;
import org.vitramu.common.definition.xml.TransitionMapper;
import org.vitramu.common.definition.xml.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Document(collection = FlowDefinition.DEFINITION_COLLECTION_NAME)
@ToString
@Getter
public class FlowDefinition extends AbstractDefinition {
    public static final String DEFINITION_COLLECTION_NAME = "definition";


    @Getter
    private Model model;

    @Getter
    private Collection<MongoDbRepositoryState> states;

    @Getter
    private Collection<MongoDbRepositoryTransition> transitions;


    @Builder
    public FlowDefinition(String id, String name) {
        super(id, name, DefinitionType.FLOW);
    }

    public FlowDefinition(String id, String name, Model model) {
        this(id, name);
        this.model = model;

        Region region = model.getPackagedElement().getRegion();
        Collection<Subvertex> subvertices = new ArrayList<>();
        Collection<Transition> transitions = new ArrayList<>();
        visit(region, subvertices, transitions);
        init(subvertices, transitions);
    }

    private void init(Collection<Subvertex> subvertices, Collection<Transition> transitions) {
        Map<String, Subvertex> subvertexMap = subvertices.stream().collect(Collectors.toMap(Subvertex::getId, Function.identity()));
        Set<String> targets = transitions.stream().map(Transition::getTarget).collect(Collectors.toSet());
        subvertices.forEach(s -> {
            s.setZeroIngoing(!targets.contains(s.getId()));
        });

        Map<Subvertex, MongoDbRepositoryState> stateMap = subvertices.stream().collect(Collectors.toMap(Function.identity(), new SubvertexToStateMapper()));
        stateMap.forEach((key, value) -> {
            value.setMachineId(name);
            key.getParentSubvertex().map(stateMap::get).ifPresent(s -> value.setParentState(s));
        });

        this.states = stateMap.values();

        Map<Transition, MongoDbRepositoryTransition> transitionMap = transitions.stream().collect(Collectors.toMap(Function.identity(), new TransitionMapper()));
        transitionMap.forEach((key, value) -> {
            value.setMachineId(name);
            value.setSource(stateMap.get(subvertexMap.get(key.getSource())));
            value.setTarget(stateMap.get(subvertexMap.get(key.getTarget())));
        });
        this.transitions = transitionMap.values();
    }

    private void visit(Region region, @NonNull final Collection<Subvertex> subvertices, @NonNull final Collection<Transition> transitions) {
        if (null == region) {
            return;
        }

        if (null != region.getTransitions()) {
            transitions.addAll(region.getTransitions());
        }
        Collection<Subvertex> subs = region.getSubvertices();
        if (null != subs) {
            subvertices.addAll(subs);
//            建立Subvertex指向父级Region的父指针
            subs.stream().forEach(subvertex -> subvertex.setParent(region));
            subs.stream()
                    .collect(ArrayList<Region>::new,
                            (list, subvertex) -> subvertex.getRegions().ifPresent(rgs -> {
                                rgs.forEach(r -> r.setParent(subvertex));
                                list.addAll(rgs);
                            }),
                            ArrayList::addAll)
                    .stream()
                    .filter(r -> null != r)
                    .forEach(r -> visit(r, subvertices, transitions));
        }
    }
}
