package org.vitramu.common.definition.xml;

import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.state.PseudoStateKind;
import org.vitramu.common.definition.xml.model.Region;
import org.vitramu.common.definition.xml.model.Subvertex;

import java.util.Optional;
import java.util.function.Function;

public class SubvertexToStateMapper implements Function<Subvertex, MongoDbRepositoryState> {

    @Override
    public MongoDbRepositoryState apply(Subvertex subvertex) {
        MongoDbRepositoryState state = new MongoDbRepositoryState();

        state.setRegion(Optional.ofNullable(subvertex.getParent()).map(Region::getName).orElse(null));
        state.setState(subvertex.getName());
        if(subvertex.isZeroIngoing() && !subvertex.getRegions().isPresent()) {
            state.setKind(PseudoStateKind.INITIAL);
            state.setInitial(true);
        }
        state.setKind(subvertex.getPseudoStateKind());

        return state;
    }
}
