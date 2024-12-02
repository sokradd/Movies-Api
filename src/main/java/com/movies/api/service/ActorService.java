package com.movies.api.service;


import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Actor;
import com.movies.api.model.Movie;
import com.movies.api.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> getActors() {
        return actorRepository.findAll();
    }

    public Optional<Actor> findActorById(Long id) {
        return actorRepository.findById(id);
    }

    public List<Actor> getActorsByName(String name) throws ResourceNotFoundException {
        return actorRepository.findActorByName(name);
    }

    public List<Movie> findMoviesByActor (Long id) throws ResourceNotFoundException {
        return actorRepository.findMoviesByActor(id);
    }

    public void deleteActorByID(Long id) {
        actorRepository.deleteById(id);
    }

    public void addNewActor(Actor actor) {
        if (isValidDate(actor.getBirthDate())) {
            throw new IllegalStateException("Invalid birth date format.");
        }
        actorRepository.saveAndFlush(actor);
    }

    public void deleteActorsByID(Long id) throws ResourceNotFoundException {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Actor with id " + id + " is not found.");
        }
    }

    public void updateActorByID(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Actor with id " + id + " is not found."));
        updates.forEach((key, value) -> {
            try {
                Field field = actor.getClass().getDeclaredField(key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, actor, value);
                    System.out.println(field);
                }
                if (field.toString().contains("birthDate")) {
                    if (isValidDate((String) value)) {
                        throw new IllegalStateException("Invalid birth date format.");
                    }
                }
                if (field.toString().contains("id")) {
                    throw new IllegalStateException("ID is immutable.");
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            actorRepository.saveAndFlush(actor);
        });
    }

    public static boolean isValidDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(date, dateFormatter);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}
