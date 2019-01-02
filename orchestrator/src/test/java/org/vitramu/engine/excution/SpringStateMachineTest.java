package org.vitramu.engine.excution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachineTest {

    @Value("classpath:simple-sequence.uml")
    Resource simpleSequence;

    @Value("classpath:simple-machine.uml")
    Resource simpleMachine;

    @Test
    public void testPapyrusStateMachineUML() throws Exception {
        UmlStateMachineModelFactory umlSMFactory = new UmlStateMachineModelFactory(simpleSequence);
        StateMachineBuilder.Builder builder = StateMachineBuilder.builder();
        builder.configureModel().withModel().factory(umlSMFactory);
        StateMachine sm = builder.build();
        sm.addStateListener(createSMListener());
        sm.start();
    }

    @Test
    public void testSimpleMachineUml() throws Exception {
        UmlStateMachineModelFactory umlSMFactory = new UmlStateMachineModelFactory(simpleMachine);
        StateMachineBuilder.Builder builder = StateMachineBuilder.builder();
        builder.configureModel().withModel().factory(umlSMFactory);
        StateMachine sm = builder.build();
        sm.addStateListener(createSMListener());
        sm.start();

    }

    public static StateMachineListener createSMListener() {
        return new StateMachineListener() {
            @Override
            public void stateChanged(State from, State to) {
//                System.out.println("State Changed from " + from + " to " + to);
            }

            @Override
            public void stateEntered(State state) {
                System.out.println("Enter State " + state.getIds());
            }

            @Override
            public void stateExited(State state) {
                System.out.println("Exit State " + state.getIds());

            }

            @Override
            public void eventNotAccepted(Message event) {

            }

            @Override
            public void transition(Transition transition) {

            }

            @Override
            public void transitionStarted(Transition transition) {

            }

            @Override
            public void transitionEnded(Transition transition) {

            }

            @Override
            public void stateMachineStarted(StateMachine stateMachine) {

            }

            @Override
            public void stateMachineStopped(StateMachine stateMachine) {

            }

            @Override
            public void stateMachineError(StateMachine stateMachine, Exception exception) {

            }

            @Override
            public void extendedStateChanged(Object key, Object value) {

            }

            @Override
            public void stateContext(StateContext stateContext) {

            }
        };
    }

}
