package com.fabiogouw.hexagonal.domain.core;

import com.fabiogouw.hexagonal.domain.ports.SimulationHandler;
import com.fabiogouw.hexagonal.domain.ports.SimulationRequest;
import com.fabiogouw.hexagonal.domain.ports.SimulationResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
public class SimulationHandlerImpl implements SimulationHandler {

    private static int lastId = 0;

    @Override
    public CompletableFuture<SimulationResponse> handle(SimulationRequest simulationRequest) {
        CompletableFuture<SimulationResponse> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            future.complete(new SimulationResponse(++lastId,
                    simulationRequest.getClienteId(),
                    0,
                    simulationRequest.getInstallments(),
                    simulationRequest.getValue() / simulationRequest.getInstallments()));
            return null;
        });
        return future;
    }
}
