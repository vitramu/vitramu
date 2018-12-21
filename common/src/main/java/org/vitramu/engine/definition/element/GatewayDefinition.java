package org.vitramu.engine.definition.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vitramu.engine.definition.AbstractDefinition;

@ToString
public class GatewayDefinition extends AbstractDefinition {

    @Setter
    @Getter
    private GatewayType gatewayType;

    public GatewayDefinition(String id, String name) {
        super(id,name,DefinitionType.GATEWAY);
    }

}