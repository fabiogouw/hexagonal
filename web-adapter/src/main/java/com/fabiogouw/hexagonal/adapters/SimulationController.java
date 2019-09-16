package com.fabiogouw.hexagonal.adapters;

import com.fabiogouw.hexagonal.ports.SimulationHandler;
import com.fabiogouw.hexagonal.ports.SimulationRequest;
import com.fabiogouw.hexagonal.ports.SimulationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Component
@RestController
@RequestMapping("/simulations")
public class SimulationController implements SimulationHandler {

    private SimulationHandler simulationHandler;

    public SimulationController(SimulationHandler simulationHandler) {
        this.simulationHandler = simulationHandler;
    }

    @PostMapping
    public @ResponseBody CompletableFuture<SimulationResponse> handle(@RequestBody SimulationRequest simulationRequest) {
        return this.simulationHandler.handle(simulationRequest);
    }
}
