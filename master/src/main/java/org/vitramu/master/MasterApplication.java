package org.vitramu.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = {"org.vitramu.master"})
@EnableWebMvc
public class MasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class);
    }
}
