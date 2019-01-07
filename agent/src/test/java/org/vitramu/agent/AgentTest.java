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
import org.vitramu.engine.constant.DefinitionState;

import java.util.HashMap;
import java.util.Map;

import static org.vitramu.common.constant.MqConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgentTest {
    public static final String FLOW_DEFINITION_ID = "F1";
    public static final String FLOW_INSTANCE_ID = "F1-1";
    public static final String FLOW_INSTANCE_ID_2 = "F1-2";
    public static final String SERVICE_NAME = "service1";
    public static final String SERVICE_INSTANCE_ID = "instance1";

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

    private Message buildStartMessage(String flowInstanceId, Message m) {
        Map<String, Object> headers = m.getMessageProperties().getHeaders();
        headers.put("flowDefinitionId", FLOW_DEFINITION_ID);
        headers.put("flowInstanceId", FLOW_INSTANCE_ID);
        headers.put("start", true);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        return m;
    }

    private Message buildTaskMessage(String taskInstanceId, String taskName, Message m) {
        Map<String, Object> headers = m.getMessageProperties().getHeaders();
        headers.put("flowDefinitionId", FLOW_DEFINITION_ID);
        headers.put("flowInstanceId", FLOW_INSTANCE_ID);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        headers.put("taskInstanceId", taskInstanceId);
        headers.put("taskName", taskName);
        return m;
    }

    @Test
    public void testStartSingleFlow() {
//        String payload = gson.toJson(new Object());
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String,Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));
    }


    @Test
    public void testStartSingleFlowMutipleTimes() {
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String,Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new Object(), m -> buildStartMessage(FLOW_INSTANCE_ID_2, m));
    }

    @Test
    public void testCompleteRequestSaving() throws InterruptedException {
//        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String,Object>(), m -> buildStartMessage(FLOW_INSTANCE_ID, m));
        this.testStartSingleFlow();
        Thread.sleep(20);
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String,Object>(), m -> buildTaskMessage("t1", DefinitionState.REQUEST_SAVING.getName(), m));
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
}
