package org.vitramu.engine.excution.element;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

public class JoinGateway extends Gateway {
    private final int indegree;

    private int hitCount;

    private boolean passed = false;
    private StateMachine<JoinGatewayState, JoinGatewayEvent> stateMachine;

    public JoinGateway(int indegree) {
        this.indegree = indegree;
        stateMachine = createJoinGatewayStateMachine();
        stateMachine.start();
    }

    private StateMachine<JoinGatewayState, JoinGatewayEvent> createJoinGatewayStateMachine() {
        StateMachineBuilder.Builder<JoinGatewayState, JoinGatewayEvent> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(JoinGatewayState.INITIAL)
                    .states(EnumSet.allOf(JoinGatewayState.class));
            builder.configureTransitions()
                    .withExternal().source(JoinGatewayState.INITIAL).target(JoinGatewayState.WAIT).event(JoinGatewayEvent.BLOCK).action(context -> this.block())
                    .and()
                    .withExternal().source(JoinGatewayState.WAIT).target(JoinGatewayState.WAIT).event(JoinGatewayEvent.BLOCK).action(context -> this.block())
                    .and()
                    .withExternal().source(JoinGatewayState.INITIAL).target(JoinGatewayState.PAST).event(JoinGatewayEvent.FINISH).action(context -> this.pass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public boolean isBlocked() {
        return !passed;
    }

    private void block() {
        passed = false;
    }
    private void pass() {
        passed = true;
    }

    public void hit() {
        // TOOD use redis to atomic increment
        this.hitCount++;
        System.out.println("Hit JoinGateway " + this.getDefinitionId() + " with count: " + hitCount);
        if(this.hitCount >= this.indegree) {
            stateMachine.sendEvent(JoinGatewayEvent.FINISH);
        } else {
            stateMachine.sendEvent(JoinGatewayEvent.BLOCK);
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
