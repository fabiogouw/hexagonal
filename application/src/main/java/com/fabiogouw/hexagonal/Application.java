package com.fabiogouw.hexagonal;

import com.fabiogouw.hexagonal.domain.ports.SimulationRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fabiogouw.hexagonal"})
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        System.out.println(Arrays.asList(applicationContext.getBeanDefinitionNames()));

        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);

        jmsTemplate.convertAndSend("DEV.QUEUE.1", new SimulationRequest("1", 2, 3));
    }

}
