package org.vitramu.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = {"org.vitramu"}, exclude = SecurityAutoConfiguration.class)
@EnableWebMvc
@EnableMongoRepositories
public class MasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class);
    }
}
