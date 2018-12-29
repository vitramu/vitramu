package org.vitramu.engine.definition.element;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vitramu.engine.definition.AbstractDefinition;

@ToString
@EqualsAndHashCode
public class GatewayDefinition extends AbstractDefinition {


    public GatewayDefinition(String id, String name, DefinitionType type) {
        super(id, name, type);
    }

}
