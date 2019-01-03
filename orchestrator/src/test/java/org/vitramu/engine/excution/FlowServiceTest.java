package org.vitramu.engine.excution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.*;
import org.vitramu.engine.excution.element.StartEvent;
import org.vitramu.engine.excution.instance.Flow;
import org.vitramu.engine.excution.service.FlowRepository;
import org.vitramu.engine.excution.service.FlowService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowServiceTest {

    @Autowired
    private FlowService flowService;

    @MockBean
    private FlowDefinitionRepository flowDefinitionRepository;
    @MockBean
    private FlowRepository flowRepository;

    @Before
    public void setUp() {


    }

    @Test
    public void startFlowInstance() {
        FlowDefinition flowDefinition = FlowDefinition.builder()
                .id("F1")
                .name("SimpleFlowDemo")
                .start(new StartEventDefinition("S0", "S0"))
                .end(new EndEventDefinition("E0", "E0"))
                .task(new TaskDefinition("T1", "RequestReceived"))
                .task(new TaskDefinition("T2", "CreateOsbBooking"))
                .task(new TaskDefinition("T3", "CreatePudBooking"))
                .task(new TaskDefinition("T4", "CreateEdjBooking"))
                .task(new TaskDefinition("T5", "FreshBookingStatus"))
                .gateway(new GatewayDefinition("GW1", "ParallelGateway", DefinitionType.GATEWAY_PARALLEL))
                .gateway(new GatewayDefinition("GW2", "ParallelGateway", DefinitionType.GATEWAY_JOIN))
                .build();

        flowDefinition.connect("S1", "S0", "T1")
                .connect("S2", "T1", "GW1")
                .connect("S3", "GW1", "T2")
                .connect("S4", "GW1", "T3")
                .connect("S5", "GW1", "T4")
                .connect("S6", "T2", "GW2")
                .connect("S7", "T3", "GW2")
                .connect("S8", "T4", "GW2")
                .connect("S9", "GW2", "T5")
                .connect("S10", "T5", "E0");

        when(flowRepository.isStarted(any())).thenReturn(false);
        when(flowDefinitionRepository.findFlowDefinitionById(any())).thenReturn(flowDefinition);
        String flowInstanceId = "Instance-1";
        String flowDefinitionId = "F1";
        StartEvent startEvent = StartEvent.builder()
                .flowDefinitionId(flowDefinitionId)
                .parentFlowInstanceId(null)
                .serviceInstanceId("Service1")
                .serviceName("OSB")
                .transactionId(flowInstanceId)
                .data("")
                .build();


    }

    @Test
    public void continousParallelGateway() {
        FlowDefinition flowDefinition = FlowDefinition.builder()
                .id("F2")
                .name("SimpleFlowDemo")
                .start(new StartEventDefinition("S0", "S0"))
                .end(new EndEventDefinition("E0", "E0"))
                .task(new TaskDefinition("T1", "RequestReceived"))
                .task(new TaskDefinition("T2", "CreateOsbBooking"))
                .task(new TaskDefinition("T3", "CreatePudBooking"))
                .task(new TaskDefinition("T4", "CreateEdjBooking"))
                .task(new TaskDefinition("T5", "FreshBookingStatus"))
                .gateway(new GatewayDefinition("GW1", "ParallelGateway", DefinitionType.GATEWAY_PARALLEL))
                .gateway(new GatewayDefinition("GW2", "ParallelGateway", DefinitionType.GATEWAY_PARALLEL))
                .gateway(new GatewayDefinition("GW3", "ParallelGateway", DefinitionType.GATEWAY_JOIN))
                .build();
        flowDefinition.connect("S1", "S0", "T1")
                .connect("S2", "T1", "GW1")
                .connect("S3", "GW1", "T2")
                .connect("S4", "GW1", "GW2")
                .connect("S5", "T2", "GW3")
                .connect("S6", "GW2", "T3")
                .connect("S7", "GW2", "T4")
                .connect("S8", "T3", "GW3")
                .connect("S9", "T4", "GW3")
                .connect("S10", "GW3", "T5")
                .connect("S11", "T5", "E0");


        when(flowRepository.isStarted(any())).thenReturn(false);
        when(flowDefinitionRepository.findFlowDefinitionById(any())).thenReturn(flowDefinition);
        String flowInstanceId = "Instance-2";
        String flowDefinitionId = "F2";
        StartEvent startEvent = StartEvent.builder()
                .flowDefinitionId(flowDefinitionId)
                .parentFlowInstanceId(null)
                .serviceInstanceId("Service1")
                .serviceName("OSB")
                .transactionId(flowInstanceId)
                .data("")
                .build();

    }
    @Test
    public void completeTask() {
    }

    @Test
    public void abortTask() {
    }

    @Test
    public void abortFlowInstance() {
    }


}