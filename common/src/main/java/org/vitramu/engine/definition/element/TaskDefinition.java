package org.vitramu.engine.definition.element;

import lombok.Setter;
import org.vitramu.engine.definition.AbstractDefinition;


public class TaskDefinition extends AbstractDefinition {

    private boolean deprecated = false;
    private String description;

    @Setter
    private CommandDefinition command;


    public TaskDefinition(String id, String name) {
        super(id,name,DefinitionType.TASK);
    }
}
