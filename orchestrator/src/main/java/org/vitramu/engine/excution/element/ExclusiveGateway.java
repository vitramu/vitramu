package org.vitramu.engine.excution.element;

public class ExclusiveGateway extends Gateway {
    @Override
    public boolean shouldWait() {
        return false;
    }
}
