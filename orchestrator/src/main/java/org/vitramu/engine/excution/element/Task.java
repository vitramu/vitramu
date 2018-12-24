package org.vitramu.engine.excution.element;

import lombok.NonNull;
import lombok.Setter;
import org.vitramu.engine.definition.element.DefinitionType;
import org.vitramu.engine.definition.element.TaskDefinition;
import org.vitramu.engine.excution.AbstractExcutableInstance;
import org.vitramu.engine.excution.TaskInstance;

public class Task extends AbstractExcutableInstance implements TaskInstance {
    @NonNull
    @Setter
    private TaskDefinition definition;

    @Override
    public String getDefinitionId() {
        return definition.getId();
    }

    @Override
    public DefinitionType getDefinitionType() {
        return definition.getType();
    }

    @Override
    public void start() {
        // call completeTask on this instance

        // persist task state

        // create and emit MQ event according task definition, flowInstanceId and input parameters
    }
}
