package org.vitramu.engine.excution.element;

import lombok.Setter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.vitramu.engine.definition.element.DefinitionType;
import org.vitramu.engine.definition.element.SequenceDefinition;
import org.vitramu.engine.excution.AbstractInstance;

import java.util.EnumSet;



public class Sequence extends AbstractInstance<SequenceDefinition> {

    private Expression condition;
    private ExpressionParser parser = new SpelExpressionParser();
    private SequenceState state = SequenceState.INITIAL;

    private StateMachine<SequenceState, SequenceEvent> stateMachine;

    @Setter
    private String dataIn;
    @Setter
    private Object transformer;
    @Setter
    private String dataOut;

    public Sequence(SequenceDefinition definition) {
        this.definition = definition;
        if (null != definition.getCondition()) {
            this.condition = parser.parseExpression(this.definition.getCondition());
        }
        stateMachine = createSequenceStateMachine();
    }
    public static StateMachine<SequenceState, SequenceEvent> createSequenceStateMachine() {
        StateMachineBuilder.Builder<SequenceState,SequenceEvent> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(SequenceState.INITIAL)
                    .states(EnumSet.allOf(SequenceState.class));
            builder.configureTransitions()
                    .withExternal().source(SequenceState.INITIAL).target(SequenceState.ENTERED).event(SequenceEvent.ENTER)
                    .and()
                    .withExternal().source(SequenceState.ENTERED).target(SequenceState.SKIPPED).event(SequenceEvent.SKIP)
                    .and()
                    .withExternal().source(SequenceState.ENTERED).target(SequenceState.TRANSFORMING).event(SequenceEvent.TRANSFORM)
                    .and()
                    .withExternal().source(SequenceState.ENTERED).target(SequenceState.SKIPPED).event(SequenceEvent.SKIP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public void on(SequenceEvent event) {
        stateMachine.sendEvent(event);
    }

    public void enter() {
    }

    public void skip() {
        state = SequenceState.SKIPPED;
    }

    public void transform() {
        // TODO json transform using transformer
        dataOut = dataIn;
    }

    public void exit() {
        state = SequenceState.EXITED;
    }

    public boolean isSkipped() {
        return SequenceState.SKIPPED.equals(state);
    }

    public boolean isExited() {
        return SequenceState.EXITED.equals(state);
    }

    // TODO use root object and context
    public Boolean getConditionValue() {
        if (null == condition) {
            return false;
        }
        return this.condition.getValue(Boolean.class);
    }

    public enum SequenceState {
        INITIAL, ENTERED, SKIPPED, TRANSFORMING, EXITED;
    }

    public enum SequenceEvent {
        ENTER, SKIP, TRANSFORM, EXIT;
    }

}
