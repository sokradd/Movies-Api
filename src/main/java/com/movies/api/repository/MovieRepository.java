package com.movies.api.repository;

import com.movies.api.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :id")
    Set<Movie> findMoviesByActor (long id);

    @Query("SELECT m FROM Movie m WHERE m.releaseYear = :year")
    Set<Movie> findMoviesByReleaseYear (String year);

    @Query("SELECT m FROM Movie m JOIN m.genres g  WHERE g.id = :id")
    List<Movie> findMoviesByGenreID (Long id);

    Page<Movie> findAll(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findMoviesByName (String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM actor_movie WHERE movie_id = :movieId", nativeQuery = true)
    void removeActorsFromMovie(@Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO actor_movie (actor_id, movie_id) VALUES (:actorId, :movieId)", nativeQuery = true)
    void addActorToMovie(@Param("actorId") Long actorId, @Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM genre_movie WHERE movie_id = :movieId", nativeQuery = true)
    void removeGenresFromMovie(@Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO genre_movie (genre_id, movie_id) VALUES (:genreId, :movieId)", nativeQuery = true)
    void addGenreToMovie(@Param("genreId") Long genreId, @Param("movieId") Long movieId);
}
