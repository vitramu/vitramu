package org.vitramu.engine.definition.element;

import lombok.ToString;
import org.vitramu.engine.definition.AbstractDefinition;

/**
 * CommandDefinition is a proxy  of CommandHandler implementation of downstream microservices in vitramu engine.
 */
@ToString
public class CommandDefinition extends AbstractDefinition {

    private String serviceName;
    private String schemaIn; // TODO use typed JSON Schema object
    private String schemaOut; // TODO use typed JSON Schema object

    public CommandDefinition(String id, String name) {
        super(id, name, DefinitionType.COMMAND);
    }
}
