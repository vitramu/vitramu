package org.vitramu.engine.excution.instance;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.constant.DefinitionState;
import org.vitramu.engine.excution.message.StartMessage;
import org.vitramu.engine.excution.message.TaskMessage;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowInstanceServiceTest {


    Gson gson;

    static final String FLOW_DEFINITION_ID = "F1";
    static final String FLOW_INSTANCE_ID = "F1-1";
    static final String FLOW_INSTANCE_ID_2 = "F1-2";
    static final String SERVICE_NAME = "service1";
    static final String SERVICE_INSTANCE_ID = "instance1";

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Before
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void startFlowInstance() {
        String payload = gson.toJson(new Object());
        StartMessage start = StartMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID)
                .parentFlowInstanceId(null)
                .serviceName(SERVICE_NAME)
                .serviceInstanceId(SERVICE_INSTANCE_ID)
                .body(new JsonParser().parse(payload))
                .build();
    }

    @Test
    public void completeRequestSaving() {
        this.startFlowInstance();

        TaskMessage taskMessage = TaskMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID)
                .taskInstanceId(UUID.randomUUID().toString())
                .taskName(DefinitionState.REQUEST_SAVING.getName())
                .aborted(false)
                .build();
    }

    @Test
    public void completeCreateOSB() {
        this.completeRequestSaving();

        TaskMessage taskMessage = TaskMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID)
                .taskInstanceId(UUID.randomUUID().toString())
                .taskName(DefinitionState.CREATE_OSB.getName())
                .aborted(false)
                .build();
    }

    @Test
    public void completeCreatePUD() {
        this.completeCreateOSB();

        TaskMessage taskMessage = TaskMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID)
                .taskInstanceId(UUID.randomUUID().toString())
                .taskName(DefinitionState.CREATE_PUD.getName())
                .aborted(false)
                .build();
    }

    @Test
    public void multipleRequestStart() {
        String payload = gson.toJson(new Object());
        StartMessage start = StartMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID)
                .parentFlowInstanceId(null)
                .serviceName(SERVICE_NAME)
                .serviceInstanceId(SERVICE_INSTANCE_ID)
                .body(new JsonParser().parse(payload))
                .build();

        StartMessage start2 = StartMessage.builder()
                .flowDefinitionId(FLOW_DEFINITION_ID)
                .flowInstanceId(FLOW_INSTANCE_ID_2)
                .parentFlowInstanceId(null)
                .serviceName(SERVICE_NAME)
                .serviceInstanceId(SERVICE_INSTANCE_ID)
                .body(new JsonParser().parse(payload))
                .build();
    }
}