package org.vitramu.engine.definition.element;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vitramu.engine.definition.AbstractDefinition;

@ToString
@EqualsAndHashCode
public class GatewayDefinition extends AbstractDefinition {

    @Setter
    @Getter
    private GatewayType gatewayType;

    public GatewayDefinition(String id, String name, GatewayType gatewayType) {
        super(id,name,DefinitionType.GATEWAY);
        this.gatewayType = gatewayType;
    }

}
