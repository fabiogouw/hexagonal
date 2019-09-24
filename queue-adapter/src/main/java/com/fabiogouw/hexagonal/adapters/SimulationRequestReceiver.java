package com.fabiogouw.hexagonal.adapters;

import com.fabiogouw.hexagonal.domain.ports.SimulationHandler;
import com.fabiogouw.hexagonal.domain.ports.SimulationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class SimulationRequestReceiver {

    private final Logger logger = LoggerFactory.getLogger(SimulationRequestReceiver.class);

    private JmsTemplate jmsTemplate;
    private SimulationHandler simulationHandler;
    private ObjectMapper objectMapper;

    public SimulationRequestReceiver(JmsTemplate jmsTemplate,
                                     SimulationHandler simulationHandler,
                                     ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.simulationHandler = simulationHandler;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "DEV.QUEUE.1", containerFactory = "myFactory")
    public void receiveMessage(Message message) {
        try {
            String text = message.getBody(String.class);
            logger.info("Received {}", text);
            SimulationRequest simulationRequest = objectMapper.readValue(text, SimulationRequest.class);
            simulationHandler.handle(simulationRequest).thenAccept(response -> {
                jmsTemplate.convertAndSend("DEV.QUEUE.2", response);
            });
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem {}", e.getMessage());
        }
    }
}
