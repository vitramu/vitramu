package org.vitramu.engine.excution;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonTest {

    @Test
    public void testJsonObject() {
        Map<String, Object> body = new HashMap<>();

        Map<String, Object> headers = new HashMap<>();
        headers.put("contentType", "application/json");
        headers.put("flowDefinitionId", "F1");
        headers.put("flowInstanceId", "f1");

        Map<String, Object> data = new HashMap<>();
        data.put("ids", Arrays.asList("id1","id2"));
        body.put("headers", headers);
        body.put("data",data);

        Gson gson  = new Gson();
        String payload = gson.toJson(body);
        System.out.println("payload: " + payload);

        JsonElement jsonObject = gson.toJsonTree(payload);
        System.out.println(jsonObject);
    }
}
