package org.vitramu.engine.excution;

import org.junit.Before;
import org.junit.Test;
import org.vitramu.engine.excution.element.StartEvent;

import static org.junit.Assert.*;

public class FlowServiceTest {

    private FlowService flowService;

    @Before
    public void setUp() {
        flowService = new FlowService();
    }
    @Test
    public void startFlowInstance() {
        StartEvent startEvent =  StartEvent.builder()
                .flowDefinitionId("D1")
                .parentFlowInstanceId(null)
                .serviceInstanceId("S1")
                .serviceName("OSB")
                .transactionId("T1")
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