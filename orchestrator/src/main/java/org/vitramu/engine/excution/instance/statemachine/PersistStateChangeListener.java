package org.vitramu.engine.excution.instance.statemachine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * The listener interface for receiving persistStateChange events.
 * The class that is interested in processing a persistStateChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPersistStateChangeListener</code> method. When
 * the persistStateChange event occurs, that object's appropriate
 * method is invoked.
 */
public interface PersistStateChangeListener {

    /**
     * Called when state needs to be persisted.
     *
     * @param state the state
     * @param message the message
     * @param transition the transition
     * @param stateMachine the state machine
     */
    void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition,
                   StateMachine<String, String> stateMachine);
}
