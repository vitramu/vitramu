package org.vitramu.agent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EurekaClientInfoTest {

    @Autowired
    DiscoveryClient client;

    @Test
    public void testClientInfoRetrieve() {
        List<String> services = client.getServices();
//        client.getInstances()
    }

}
