package org.vitramu.agent.rest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = {"/agent"})
@RestController()
public class EurekaClientInfoController implements ApplicationContextAware {
    private DiscoveryClient client;
    private ApplicationContext context;

    @Autowired
    public EurekaClientInfoController(DiscoveryClient client) {
        this.client = client;
    }

    @Value("${spring.application.name}")
    private String serviceName;
    @GetMapping("/")
    public String home() {
        return "Hello, Agent";
    }

    @GetMapping("/registryInfo")
    public List<ServiceInstance> list() {
        List<String> services = client.getServices();
        List<ServiceInstance> instances = client.getInstances(serviceName);
        return instances;
    }

    @Autowired
    private EurekaInstanceConfigBean instanceConfigBean;
    @GetMapping("/registryBeanInfo")
    public EurekaInstanceConfigBean whoami() {
        return instanceConfigBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
