package com.movies.api.service;


import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Genre;
import com.movies.api.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreByID(Long id) {
        return genreRepository.findById(id);
    }

    public void addNewGenre(Genre genre) {
        genreRepository.saveAndFlush(genre);
    }

    public void deleteGenreByID(Long id) throws ResourceNotFoundException {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Genre with id " + id + " is not found");
        }
    }

    public void updateGenreByID(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre with id " + id + " is not found"));

        updates.forEach((key, value) -> {
            try {
                Field field = genre.getClass().getDeclaredField(key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, genre, value);
                }
                if (field.toString().contains("id")){
                    throw new IllegalStateException("ID is immutable");
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            genreRepository.saveAndFlush(genre);

        });
    }

}
