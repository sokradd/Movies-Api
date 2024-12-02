package com.movies.api.controller;

import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Actor;
import com.movies.api.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping//getting list of actors
    public Object getActors(@RequestParam(required = false) String name) throws ResourceNotFoundException {
        List<Actor> actors = actorService.getActors();
        if (name != null) {
            if (actorService.getActorsByName(name).isEmpty()) {
                return ResponseEntity.status(404).body("Actor not found.");
            }
            return actorService.getActorsByName(name);
        }
        return actors;
    }

    @GetMapping("/{id}") // getting actor by id
    public Object findActorById(@PathVariable Long id) {
        Optional<Actor> actor = actorService.findActorById(id);
        if(actor.isPresent()) {
            return actor;
        }
        return ResponseEntity.status(404).body("Actor not found.");
    }

    @PostMapping() // adding actor
    public ResponseEntity<String> registerNewActor(@RequestBody Actor actor) {
        actorService.addNewActor(actor);
        return ResponseEntity.status(201).body("Actor added:\n" + actor.toString());
    }

    @DeleteMapping(path = "{id}") // deleting actor / deleting actor by id / force deleting
    public Optional<ResponseEntity<String>> deleteActor(@PathVariable Long id, @RequestParam boolean force) throws ResourceNotFoundException, IllegalStateException {
        if(actorService.findActorById(id).isEmpty()) {
            return Optional.of(ResponseEntity.status(404).body("Actor not found."));
        }
        if(force) {
            actorService.deleteActorsByID(id);
            return Optional.of(ResponseEntity.status(204).body("Actor with id " + id + " deleted successfully."));
        } else {
            if (actorService.findActorById(id).get().getAssignedMovies() != null ) {
                Long movieCount = actorService.findActorById(id).get().getAssignedMovies().stream().count();
                if(movieCount > 0) {
                    return Optional.of(ResponseEntity.status(400).body("Unable to delete actor '" + actorService.findActorById(id).get().getName() + "' as they are associated with " + movieCount + " movies."));
                } else {
                    actorService.deleteActorByID(id);
                    return Optional.of(ResponseEntity.status(204).body("Actor with id " + id + " deleted successfully"));
                }
            }
        }
        return Optional.of(ResponseEntity.status(404).body("Actor not found."));

    }

    @PatchMapping(path = "{id}") // update info actor by id
    public Optional<ResponseEntity<String>> updateActorByID(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        actorService.updateActorByID(id, updates);
        return Optional.of(ResponseEntity.status(200).body("Actor updated successfully"));
    }
}