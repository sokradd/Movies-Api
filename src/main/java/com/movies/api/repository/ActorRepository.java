package com.movies.api.repository;

import com.movies.api.model.Actor;
import com.movies.api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    @Query("SELECT a FROM Actor a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Actor> findActorByName(String name);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :id")
    List<Movie> findMoviesByActor(Long id);
}
