package com.movies.api.controller;

import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Genre;
import com.movies.api.repository.GenreRepository;
import com.movies.api.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAGenres() {
        return genreService.getGenres();
    }

    @GetMapping(path = "{id}")
    public Object getGenreByID(@PathVariable Long id) {
        Optional<Genre> genre = genreService.getGenreByID(id);
        if (genre.isPresent()) {
            return genre;
        }
        return ResponseEntity.status(404).body("Genre not found");

    }
    @PostMapping()
    public ResponseEntity<String> registerNewGenre(@RequestBody Genre genre) {
        genreService.addNewGenre(genre);
        return ResponseEntity.status(201).body("Genre added:\n" + genre.toString());
    }
    @DeleteMapping(path = "{id}")
    public Optional<ResponseEntity<String>> deleteGenreByID(@PathVariable Long id, @RequestParam boolean force) throws ResourceNotFoundException {
        if (genreService.getGenreByID(id).isEmpty()) {
            return Optional.of(ResponseEntity.status(404).body("Genre not found"));
        }
        if (force) {
            genreService.deleteGenreByID(id);
            return Optional.of(ResponseEntity.status(204).body("Genre with id " + id + " deleted successfully"));
        } else {
            if (genreService.getGenreByID(id).get().getAssignedMovies() != null) {
                Long movieCount = genreService.getGenreByID(id).get().getAssignedMovies().stream().count();
                if (movieCount > 0) {
                    return Optional.of(ResponseEntity.status(400).body("Unable to delete genre '" + genreService.getGenreByID(id).get().getName() + "' as they are associated with " + movieCount +" movies"));
                } else {
                    genreService.deleteGenreByID(id);
                    return Optional.of(ResponseEntity.status(204).body("Genre with id " + id + " deleted successfully"));
                }

            }
        }
        return Optional.of(ResponseEntity.status(404).body("Genre not found"));
    }

    @PatchMapping(path = "{id}")
    public Optional<ResponseEntity<String>> updateGenreByID(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {
        genreService.updateGenreByID(id, updates);
        return Optional.of(ResponseEntity.status(200).body("Genre updated successfully"));
    }
}
