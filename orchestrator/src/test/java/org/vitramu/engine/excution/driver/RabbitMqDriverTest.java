package org.vitramu.engine.excution.driver;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqDriverTest {

    @Autowired
    RabbitMqDriver rabbitMqDriver;

    Gson gson;

    static final String FLOW_DEFINITION_ID = "F1";
    static final String FLOW_INSTANCE_ID = "F1";
    static final String SERVICE_NAME = "service1";
    static final String SERVICE_INSTANCE_ID = "instance1";

    @Before
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void onRequestSavedTaskMessage() {
        this.onStartMessage();

        Map<String, Object> headers = new HashMap<>();
        headers.put("flowDefinitionId", FLOW_DEFINITION_ID);
        headers.put("flowInstanceId", FLOW_INSTANCE_ID);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        headers.put("taskName", "REQUEST_SAVING");
        String payload = gson.toJson(new Object());
//        rabbitMqDriver.onMessage(payload, headers);
    }

    @Test
    public void onStartMessage() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("flowDefinitionId", FLOW_DEFINITION_ID);
        headers.put("flowInstanceId", FLOW_INSTANCE_ID);
        headers.put("serviceName", SERVICE_NAME);
        headers.put("serviceInstanceId", SERVICE_INSTANCE_ID);
        headers.put("start", true);
        String payload = gson.toJson(new Object());

//        rabbitMqDriver.onMessage(payload, headers);

    }
}