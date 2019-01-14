package org.vitramu.common.definition.adapter;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.springframework.statemachine.config.model.StateData;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;

@AllArgsConstructor
public class MongoDbRepositoryStateAdapter extends MongoDbRepositoryState {

    @NonNull
    @Setter
    @Delegate()
    private StateData<String,String> state;
    public MongoDbRepositoryStateAdapter() {

    }


}
