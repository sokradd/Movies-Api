package com.movies.api.service;


import com.movies.api.exception.ResourceNotFoundException;
import com.movies.api.model.Actor;
import com.movies.api.model.Genre;
import com.movies.api.model.Movie;
import com.movies.api.repository.ActorRepository;
import com.movies.api.repository.GenreRepository;
import com.movies.api.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;


    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    public List<Movie> getMovies() throws ResourceNotFoundException {
        List<Movie> movie = movieRepository.findAll();
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("Movies not found");
        }
        return movie;
    }

    public Optional<Movie> getMovieByID(Long id) throws ResourceNotFoundException {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("Movie with id " + id + " is not found");
        }
        return movie;
    }

    public Set<Actor> getMovieActors (long id) throws ResourceNotFoundException {
        Movie movie = getMovieByID(id).orElseThrow(() -> new ResourceNotFoundException("Movie with id " + id + " is not found"));
        return movie.getActors();
    }

    public Set<Movie> findMoviesByActor(long id) throws ResourceNotFoundException {
        return movieRepository.findMoviesByActor(id);
    }

    public List<Movie> findMoviesByGenreID(Long id) throws ResourceNotFoundException {
        return movieRepository.findMoviesByGenreID(id);
    }

    public Set<Movie> findMoviesByReleaseYear(String year) throws ResourceNotFoundException {
        return movieRepository.findMoviesByReleaseYear(year);
    }

    public List<Movie> getMoviesByTitle(String title) throws ResourceNotFoundException {
        return movieRepository.findMoviesByName(title);
    }

    public void addNewMovie(Movie movie){

        Set<Actor> actors = movie.getActors();
        Set<Genre> genres = movie.getGenres();
        movieRepository.saveAndFlush(movie);
        for (Actor actor : actors) {
            movieRepository.addActorToMovie(actor.getId(), movie.getId());
        }
        for (Genre genre : genres) {
            movieRepository.addGenreToMovie(genre.getId(), movie.getId());
        }

    }

    public void deleteMovieByID(Long id) throws ResourceNotFoundException {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Movie with id " + id + " is not found");
        }
    }

    public Movie updateMovieByID(Long id, Movie updatedMovie) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isEmpty()) {
            throw new EntityNotFoundException("Movie not found");
        }

        Movie existingMovie = optionalMovie.get();

        if (updatedMovie.getId() != null) {
            throw new IllegalStateException("ID is immutable");
        }

        if (updatedMovie.getTitle() != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getReleaseYear() != null) {
            existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        }
        if (updatedMovie.getDuration() != null) {
            existingMovie.setDuration(updatedMovie.getDuration());
        }

        if (updatedMovie.getActors() != null && !updatedMovie.getActors().isEmpty()) {
            movieRepository.removeActorsFromMovie(id);
            for (Actor actor : updatedMovie.getActors()) {
                movieRepository.addActorToMovie(actor.getId(), id);
            }
        }

        if (updatedMovie.getGenres() != null && !updatedMovie.getGenres().isEmpty()) {
            movieRepository.removeGenresFromMovie(id);
            for (Genre genre : updatedMovie.getGenres()) {
                movieRepository.addGenreToMovie(genre.getId(), id);
            }
        }

        return movieRepository.save(existingMovie);
    }

}
