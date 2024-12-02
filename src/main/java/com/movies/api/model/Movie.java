package com.movies.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "movie")
public class Movie {

    @Id
    @SequenceGenerator(
            name = "movie_sequence",
            sequenceName = "movie_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_sequence"
    )
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String releaseYear;

    @NonNull
    private LocalTime duration;

    @JsonIgnoreProperties("assignedMovies")
    @ManyToMany(mappedBy = "assignedMovies")
    private Set<Genre> genres = new HashSet<>();

    @JsonIgnoreProperties("assignedMovies")
    @ManyToMany(mappedBy = "assignedMovies")
    private Set<Actor> actors = new HashSet<>();

    public Movie() {
    }

    public Movie(@NonNull String title, @NonNull String releaseYear, @NonNull LocalTime duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Movie(Long id, @NonNull String title, @NonNull String releaseYear, @NonNull LocalTime duration, Set<Genre> genres, Set<Actor> actors) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.genres = genres;
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", duration='" + duration + '\'' +
                ", actorSet=" + actors +
                '}';
    }
}


