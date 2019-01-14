package org.vitramu.common.definition.element;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.vitramu.common.definition.AbstractDefinition;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class TaskDefinition extends AbstractDefinition {

    private boolean deprecated = false;
    @Setter
    private String description;

    @Setter
    private CommandDefinition command;


    public TaskDefinition(String id, String name) {
        super(id, name, DefinitionType.TASK);
    }

}
