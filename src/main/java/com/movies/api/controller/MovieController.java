package com.movies.api.controller;


import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Actor;
import com.movies.api.model.Movie;
import com.movies.api.repository.MovieRepository;
import com.movies.api.service.ActorService;
import com.movies.api.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService movieService, ActorService actorService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false) Long actor,
                                 @RequestParam(required = false) String year,
                                 @RequestParam(required = false) Long genre,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size) throws ResourceNotFoundException, IllegalStateException {
        List<Movie> movies = movieService.getMovies();
        try {
            if (movies.isEmpty()) {
                throw new ResourceNotFoundException("Movie not found");
            }
            if (year != null) {
                movies = movies.stream().filter(movie -> filterMovieByYear(movie, year)).collect(Collectors.toList());
            }
            if (actor != null) {
                return actorService.findMoviesByActor(actor);
            }
            if (genre != null) {
                return movieService.findMoviesByGenreID(genre);
            }
            if (page != null) {
                Pageable pageable = PageRequest.of(page, size);
                if (size > movies.size()){
                    throw new IllegalStateException("Page size exceeded");
                } else {
                    System.out.println(movies.size());
                    return movieRepository.findAll(pageable).toList();
                }
            }

        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return movies;
    }
    @GetMapping(path = "/search")
    public List<Movie> getMoviesByName(@RequestParam String title) throws ResourceNotFoundException {
        List<Movie> movies = movieService.getMoviesByTitle(title);
        if (!movies.isEmpty()) {
            return movies;
        } else {
            throw new ResourceNotFoundException("Movies not found");
        }
    }

    public boolean filterMovieByYear(Movie movie, String year){
        return movie.getReleaseYear().equals(year);
    }
    public boolean filterMovieByGenre(Movie movie, Integer genre){
        return movie.getGenres().toString().contains("id=" + genre);
    }

    @GetMapping(path = "{id}")
    public Movie getMovieByID(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Movie> movie = movieService.getMovieByID(id);
        return movie.orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @GetMapping(path = "{id}/actors")
    public Set<Actor> getMovieActors(@PathVariable long id) throws ResourceNotFoundException {
        Optional<Movie> movie = movieService.getMovieByID(id);
        if (movie.isPresent()) {
            return movie.get().getActors();
        } else {
            throw new ResourceNotFoundException("Movie not found");
        }
    }

    @PostMapping()
    public ResponseEntity<String> registerNewMovie(@RequestBody Movie movie) {
        movieService.addNewMovie(movie);
        return ResponseEntity.status(201).body("Movie added:\n" + movie.toString());
    }

    @DeleteMapping(path = "{id}")
    public Optional<ResponseEntity<String>> deleteMovieByID(@PathVariable Long id,
                                                            @RequestParam boolean force) throws ResourceNotFoundException {
        if (force) {
            movieService.deleteMovieByID(id);
            return Optional.of(ResponseEntity.status(204).body("Movie with id " + id + " deleted successfully"));
        }
        return Optional.of(ResponseEntity.status(204).body("Movie not found"));
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<Movie> updateMovieByID(
            @PathVariable Long id,
            @RequestBody Movie updatedMovie) {
        try {
            Movie resultMovie = movieService.updateMovieByID(id, updatedMovie);
            return ResponseEntity.ok(resultMovie);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}