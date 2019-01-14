package org.vitramu.engine.excution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.ObjectStateMachineFactory;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.common.constant.DefinitionState;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;
import org.vitramu.engine.excution.instance.statemachine.FlowStateMachineInterceptor;

import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachineUmlTest {

    @Value("classpath:OSB-Machine.uml")
    Resource osbMachineUml;
    static final String MACHINE_ID = "SM-UML-1";

    @Test
    public void testSimpleMachineUml() throws Exception {
        UmlStateMachineModelFactory modelFactory = new UmlStateMachineModelFactory(osbMachineUml);
        StateMachineModel<String, String> osbModel = modelFactory.build(MACHINE_ID);

        ObjectStateMachineFactory<String, String> factory = new ObjectStateMachineFactory<>(osbModel, modelFactory);

        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
        builder.configureModel().withModel().factory(modelFactory);
        builder.configureConfiguration().withConfiguration().machineId(MACHINE_ID);
        StateMachine<String, String> sm1 = builder.build();

        sm1.getStateMachineAccessor().withRegion().addStateMachineInterceptor(new FlowStateMachineInterceptor());
        sm1.addStateListener(new FlowEngineEventListener());

        String[] events = new String[]{"INITIALIZED", DefinitionState.REQUEST_SAVING.getName(), DefinitionState.CREATE_OSB.getName(), DefinitionState.CREATE_PUD.getName()/*, DefinitionState.CREATE_EW.getName() */};
        sm1.start();
        Arrays.stream(events)
                .forEach(event -> sm1.sendEvent(event));
    }

}
