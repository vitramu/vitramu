package org.vitramu.engine.excution.element;

public class JoinGateway extends Gateway {
    @Override
    public boolean shouldWait() {
        return false;
    }
}
