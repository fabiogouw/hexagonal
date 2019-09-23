package com.fabiogouw.hexagonal.adapters;

import com.fabiogouw.hexagonal.domain.ports.SimulationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SimulationRequestReceiver {

    private final Logger logger = LoggerFactory.getLogger(SimulationRequestReceiver.class);

    @JmsListener(destination = "DEV.QUEUE.1", containerFactory = "myFactory")
    //public void receiveMessage(SimulationRequest simulationRequest) {
    public void receiveMessage(String simulationRequest) {
        logger.info("Received <" + simulationRequest + ">");
    }
}
