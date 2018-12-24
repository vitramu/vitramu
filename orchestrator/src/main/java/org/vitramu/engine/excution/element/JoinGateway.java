package org.vitramu.engine.excution.element;

public class JoinGateway extends Gateway {
    private final int indegree;

    private int hitCount;

    public JoinGateway(int indegree) {
        this.indegree = indegree;
    }
    @Override
    public boolean shouldWait() {
        return hitCount == 0 || hitCount <indegree;
    }

    public void hit() {
        // TOOD use redis to atomic increment
        System.out.println("Hit JoinGateway "+this.getDefinitionId()+" with count: "+hitCount);
        this.hitCount++;
    }
}
