package org.vitramu.agent;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.common.constant.DefinitionState;

import java.util.HashMap;
import java.util.Map;

import static org.vitramu.common.constant.DefinitionState.CREATE_OSB;
import static org.vitramu.common.constant.DefinitionState.CREATE_PUD;
import static org.vitramu.common.constant.DefinitionState.REFRESH_STATUS;
import static org.vitramu.common.constant.MqConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgentTest {

    Gson gson;


    @Autowired
    private ConnectionFactory connectionFactory;

    private RabbitTemplate rabbitTemplate;

    @Before
    public void setUp() {
        gson = new Gson();
        rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(EVENT_EXCHANGE_NAME);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    static final String OSB_SM = "OSB-Machine-2";
    static final String CONTROLLER_SM = "Controller-Definition-Test-1";
    private Message buildStartMessage(String flowInstanceId, Message m) {
        Map<String, Object> headers = m.getMessageProperties().getHeaders();
        headers.put("flowDefinitionId", CONTROLLER_SM);
        headers.put("flowInstanceId", flowInstanceId);
        headers.put("start", true);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        return m;
    }

    private Message buildTaskMessage(String flowInstanceId, String taskInstanceId, String taskName, Message m) {
        Map<String, Object> headers = m.getMessageProperties().getHeaders();
        headers.put("flowDefinitionId", CONTROLLER_SM);
        headers.put("flowInstanceId", flowInstanceId);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        headers.put("taskInstanceId", taskInstanceId);
        headers.put("taskName", taskName);
        return m;
    }

    @Test
    public void testStartSingleFlow() {
//        String payload = gson.toJson(new Object());
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));
    }


    @Test
    public void testStartSingleFlowMutipleTimes() {
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new Object(), m -> buildStartMessage(FLOW_INSTANCE_ID_2, m));
    }

    @Test
    public void testCompleteRequestSaving() throws InterruptedException {
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID, "t1", DefinitionState.REQUEST_SAVING.getName(), m));
    }

    @Test
    public void testCompleteCreateOSB() {

    }

    @Test
    public void testCompleteCreatePUD() {

    }

    @Test
    public void testCompleteCreateEW() {

    }

    @Test
    public void testCompleteRefreshStatus() {

    }

    @Test
    public void testTwoInstanceSwitch() throws InterruptedException {
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID_2, m));
        Thread.sleep(1000);


        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID, "t11", DefinitionState.REQUEST_SAVING.getName(), m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID_2, "t21", DefinitionState.REQUEST_SAVING.getName(), m));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID, "t12", DefinitionState.CREATE_OSB.getName(), m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID_2, "t22", DefinitionState.CREATE_PUD.getName(), m));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID, "t13", DefinitionState.CREATE_PUD.getName(), m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID_2, "t23", DefinitionState.CREATE_OSB.getName(), m));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID, "t14", DefinitionState.REFRESH_STATUS.getName(), m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), m -> buildTaskMessage(FLOW_INSTANCE_ID_2, "t24", DefinitionState.REFRESH_STATUS.getName(), m));
    }
}
