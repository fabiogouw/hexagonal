package com.fabiogouw.hexagonal.adapters;

import com.fabiogouw.hexagonal.ports.SimulationHandler;
import com.fabiogouw.hexagonal.ports.SimulationRequest;
import com.fabiogouw.hexagonal.ports.SimulationResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
public class SimulationHandlerImpl implements SimulationHandler {
    @Override
    public CompletableFuture<SimulationResponse> handle(SimulationRequest simulationRequest) {
        CompletableFuture<SimulationResponse> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            future.complete(new SimulationResponse(simulationRequest.getClienteId(),
                    0,
                    simulationRequest.getInstallments(),
                    simulationRequest.getValue() / simulationRequest.getInstallments()));
            return null;
        });
        return future;
    }
}
