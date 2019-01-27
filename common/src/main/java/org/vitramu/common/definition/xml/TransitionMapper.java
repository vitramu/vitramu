package org.vitramu.common.definition.xml;

import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.statemachine.transition.TransitionKind;
import org.vitramu.common.definition.xml.model.Transition;

import java.util.function.Function;

public class TransitionMapper implements Function<Transition, MongoDbRepositoryTransition> {
    @Override
    public MongoDbRepositoryTransition apply(Transition transition) {
        MongoDbRepositoryTransition repositoryTransition = new MongoDbRepositoryTransition();
        repositoryTransition.setEvent(transition.getName());
        repositoryTransition.setKind(TransitionKind.EXTERNAL);
        return repositoryTransition;
    }
}
