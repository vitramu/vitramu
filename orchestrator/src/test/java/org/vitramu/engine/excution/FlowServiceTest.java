package org.vitramu.engine.excution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.*;
import org.vitramu.engine.excution.element.StartEvent;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowServiceTest {

    private FlowService flowService;

    private FlowDefinition flowDefinition;

    @MockBean
    private FlowDefinitionRepository flowDefinitionRepository;
    @MockBean
    private FlowRepository flowRepository;
    @Before
    public void setUp() {
        flowService = new FlowService();
        flowDefinition = FlowDefinition.builder()
                .id("F1")
                .name("SimpleFlowDemo")
                .start(new StartEventDefinition("S0","S0"))
                .end(new EndEventDefinition("E0", "E0"))
                .task(new TaskDefinition("T1", "RequestReceived"))
                .task(new TaskDefinition("T2", "CreateOsbBooking"))
                .task(new TaskDefinition("T3", "CreatePudBooking"))
                .task(new TaskDefinition("T4", "FreshBookingStatus"))
                .gateway(new GatewayDefinition("GW1", "ParallelGateway", GatewayType.PARALLEL))
                .gateway(new GatewayDefinition("GW2", "ParallelGateway", GatewayType.JOIN))
                .build();

        flowDefinition.connect("S1", "S0", "T1")
                .connect("S2", "T1", "GW1")
                .connect("S3", "GW1", "T2")
                .connect("S4", "GW1", "T3")
                .connect("S5", "GW1", "T4")
                .connect("S6",  "T2","GW2")
                .connect("S7",  "T3","GW2")
                .connect("S8", "T4", "GW2")
                .connect("S9", "GW2", "T5")
                .connect("S10", "T5", "E0");

    }
    @Test
    public void startFlowInstance() {
        when(flowRepository.isStarted(any())).thenReturn(false);
        StartEvent startEvent =  StartEvent.builder()
                .flowDefinitionId("F1")
                .parentFlowInstanceId(null)
                .serviceInstanceId("S1")
                .serviceName("OSB")
                .transactionId("Instance-1")
                .data("")
                .build();
        flowService.startFlowInstance(startEvent);
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