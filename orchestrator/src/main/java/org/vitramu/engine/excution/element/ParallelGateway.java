package org.vitramu.engine.excution.element;

public class ParallelGateway extends Gateway {

    @Override
    public boolean shouldWait() {
        return false;
    }

}
