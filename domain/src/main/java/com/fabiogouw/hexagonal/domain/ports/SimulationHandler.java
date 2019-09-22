package com.fabiogouw.hexagonal.domain.ports;

import java.util.concurrent.CompletableFuture;

public interface SimulationHandler {
    CompletableFuture<SimulationResponse> handle(SimulationRequest simulationRequest);
}