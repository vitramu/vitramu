package org.vitramu.agent;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.vitramu.agent.util.MessageBuilder;
import org.vitramu.common.definition.element.TaskDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.vitramu.common.constant.DefinitionState.*;
import static org.vitramu.common.constant.MqConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeployTest {

    Gson gson;


    @Autowired
    private ConnectionFactory connectionFactory;

    private RabbitTemplate rabbitTemplate;

    private RestTemplate restTemplate;

    static final String CREATE_URL = "http://localhost:5000/flow/definition/";
    static final String UPDATE_URL = "http://localhost:5000/flow/definition/{id}";
    static final String DEPLOY_URL = "http://localhost:5000/flow/definition/{id}/deploy";
    @Value("classpath:OSB-Machine.uml")
    private Resource osbMachineUml;

    @Before
    public void setUp() throws IOException {
         restTemplate = new RestTemplate();
        gson = new Gson();
        rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(EVENT_EXCHANGE_NAME);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());


//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("definitionId", MACHINE_ID);
//        HttpEntity createRequest = new HttpEntity(requestBody);
//        ResponseEntity<String> response = restTemplate.postForEntity(CREATE_URL, createRequest, String.class);
//
//        Map<String, String> uriVariables = new HashMap<>();
//        uriVariables.put("id", response.getBody());
//
//        HttpHeaders updateHeaders = new HttpHeaders();
//        updateHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, Object> updateBody
//                = new LinkedMultiValueMap<>();
//        updateBody.add("definition", osbMachineUml.getFile());
//        HttpEntity<MultiValueMap<String, Object>> updateRequest
//                = new HttpEntity<>(updateBody, updateHeaders);
//
//        restTemplate.put(UPDATE_URL, updateRequest, uriVariables);
//
//        restTemplate.postForEntity(DEPLOY_URL, null, String.class, uriVariables);

    }

    static final String MACHINE_ID = "OSB-Machine-1";


    @Test
    public void testTwoInstanceSwitch() throws InterruptedException {
        Function<String, MessagePostProcessor> startMppCreator = flowInstanceId -> m -> {
            MessageBuilder.with(m.getMessageProperties().getHeaders())
                    .withStart()
                    .withFlow(MACHINE_ID, flowInstanceId)
                    .withService(SERVICE_NAME, SERVICE_INSTANCE_ID);
            return m;
        };

        BiFunction<String, TaskDefinition, MessagePostProcessor> taskMppCreator = (flowInstanceId, task) -> m -> {
            MessageBuilder.with(m.getMessageProperties().getHeaders())
                    .withFlow(MACHINE_ID, flowInstanceId)
                    .withService(SERVICE_NAME, SERVICE_INSTANCE_ID)
                    .withTask(task.getId(), task.getName());
            return m;
        };

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), startMppCreator.apply(FLOW_INSTANCE_ID));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), startMppCreator.apply(FLOW_INSTANCE_ID_2));
        Thread.sleep(1000);


        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID, REQUEST_SAVING));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID_2, REQUEST_SAVING));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID, CREATE_OSB));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID_2, CREATE_PUD));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID, CREATE_PUD));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID_2, CREATE_OSB));
        Thread.sleep(1000);

        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID, REFRESH_STATUS));
        rabbitTemplate.convertAndSend(EVENT_ROUTING_KEY, new HashMap<String, Object>(), taskMppCreator.apply(FLOW_INSTANCE_ID_2, REFRESH_STATUS));
    }
}
