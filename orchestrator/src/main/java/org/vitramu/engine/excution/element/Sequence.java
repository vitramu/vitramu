package org.vitramu.engine.excution.element;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.vitramu.engine.definition.element.SequenceDefinition;
import org.vitramu.engine.excution.AbstractInstance;

import java.util.EnumSet;


public class Sequence extends AbstractInstance<SequenceDefinition> {

    private Expression condition;
    private ExpressionParser parser = new SpelExpressionParser();
    private SequenceState state = SequenceState.INITIAL;
    public Sequence(SequenceDefinition definition) {
        this.definition = definition;
        if (null != definition.getCondition()) {
            this.condition = parser.parseExpression(this.definition.getCondition());
        }
    }

    public void visit() {
        state = SequenceState.VISITTED;
    }
    public void skip() {
        state = SequenceState.SKIPPED;
    }

    public Boolean getConditionValue() {
        if (null == condition) {
            return false;
        }
        return this.condition.getValue(Boolean.class);
    }

    public static enum SequenceState {
        INITIAL, VISITTED, SKIPPED;
    }



}
