package org.vitramu.engine.excution.element;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

public class JoinGateway extends Gateway {
    private final int indegree;

    private int hitCount;

    private StateMachine<JoinGatewayState, JoinGatewayEvent> stateMachine;

    public JoinGateway(int indegree) {
        this.indegree = indegree;
        stateMachine = createJoinGatewayStateMachine();
        stateMachine.start();
    }

    private static StateMachine<JoinGatewayState, JoinGatewayEvent> createJoinGatewayStateMachine() {
        StateMachineBuilder.Builder<JoinGatewayState, JoinGatewayEvent> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(JoinGatewayState.INITIAL)
                    .states(EnumSet.allOf(JoinGatewayState.class));
            builder.configureTransitions()
                    .withExternal().source(JoinGatewayState.INITIAL).target(JoinGatewayState.WAIT).event(JoinGatewayEvent.BLOCK)
                    .and()
                    .withExternal().source(JoinGatewayState.INITIAL).target(JoinGatewayState.PAST).event(JoinGatewayEvent.FINISH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    @Override
    public boolean shouldWait() {
        return JoinGatewayState.WAIT.equals(stateMachine.getState());
    }

    public void hit() {
        // TOOD use redis to atomic increment
        System.out.println("Hit JoinGateway " + this.getDefinitionId() + " with count: " + hitCount);
        this.hitCount++;
        if(this.hitCount >= this.indegree) {
            stateMachine.sendEvent(JoinGatewayEvent.FINISH);
        }
    }

    public enum JoinGatewayState {
        INITIAL, WAIT, PAST;
    }

    public enum JoinGatewayEvent {
        BLOCK,
        FINISH
    }
}
