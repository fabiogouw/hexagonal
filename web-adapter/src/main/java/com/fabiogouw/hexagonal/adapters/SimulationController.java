package com.fabiogouw.hexagonal.adapters;

import com.fabiogouw.hexagonal.domain.ports.SimulationHandler;
import com.fabiogouw.hexagonal.domain.ports.SimulationRequest;
import com.fabiogouw.hexagonal.domain.ports.SimulationResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
@RestController
@RequestMapping("/simulations")
public class SimulationController {

    private static final int PAGE_SIZE = 5;
    private SimulationHandler simulationHandler;
    private ConcurrentHashMap<Integer, SimulationResponse> simulations = new ConcurrentHashMap<>();

    public SimulationController(SimulationHandler simulationHandler) {
        this.simulationHandler = simulationHandler;
    }

    @Async("webExecutor")
    @PostMapping
    public CompletableFuture<ResponseEntity> post(HttpServletRequest request, @RequestBody SimulationRequest simulationRequest) {
        return this.simulationHandler.handle(simulationRequest).thenApply(r -> {
            final int id = r.getId();
            simulations.putIfAbsent(id, r);
            URI uri = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(id).toUri();
            return ResponseEntity.created(uri).build();
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") final long id) {
        if(simulations.containsKey(id)) {
            simulations.remove(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = { "page" })
    public ResponseEntity<Resources<Resource<SimulationResponse>>> getAll(@RequestParam("page") int page) {
        final int pageIndex = (page - 1) * PAGE_SIZE;
        final SimulationResponse[] simulations = Arrays.copyOfRange(this.simulations.values().toArray(), pageIndex, PAGE_SIZE, SimulationResponse[].class);
        List<Resource<SimulationResponse>> simulationResources =
                Stream.of(simulations)
                        .filter(s -> s != null)
                        .map(this::configureSimulationResponse)
                        .collect(Collectors.toList());
        Link selfRel = linkTo(methodOn(SimulationController.class).getAll(page)).withSelfRel();
        Resources<Resource<SimulationResponse>> resources = new Resources<>(simulationResources, selfRel);
        if(page > 1) {
            Link previousLink = linkTo(methodOn(SimulationController.class).getAll(page - 1)).withRel("previous");
            resources.add(previousLink);
        }
        if(this.simulations.size() > page * PAGE_SIZE) {
            Link nextLink = linkTo(methodOn(SimulationController.class).getAll(page + 1)).withRel("next");
            resources.add(nextLink);
        }
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<SimulationResponse>> get(@PathVariable final int id) {
        if(simulations.containsKey(id)) {
            Resource<SimulationResponse> resource = configureSimulationResponse(simulations.get(id));
            return ResponseEntity.ok(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    private Resource<SimulationResponse> configureSimulationResponse(SimulationResponse simulation) {
        Resource<SimulationResponse> resource = new Resource<>(simulation);
        Link applyLink = linkTo(methodOn(SimulationController.class).apply(0)).withRel("apply").withTitle("Apply for the loan");
        resource.add(applyLink);
        return resource;
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity apply(@PathVariable final int id) {
        return ResponseEntity.badRequest().build();
    }
}
