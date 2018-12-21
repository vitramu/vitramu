package org.vitramu.engine.excution.element;

import org.vitramu.engine.definition.element.GatewayDefinition;
import org.vitramu.engine.definition.element.GatewayType;
import org.vitramu.engine.excution.AbstractExcutableInstance;
import org.vitramu.engine.excution.GatewayInstance;

public abstract class Gateway extends AbstractExcutableInstance<GatewayDefinition> implements GatewayInstance {

    @Override
    public GatewayType getGatewayType() {
        return getDefinition().getGatewayType();
    }
    public abstract boolean shouldWait();

}
