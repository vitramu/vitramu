package org.vitramu.engine.excution.element;

public class InclusiveGateway extends Gateway {
    @Override
    public boolean shouldWait() {
        return false;
    }
}
