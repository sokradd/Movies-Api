# Movies API

A RESTful API for managing a movie database, developed using Spring Boot and JPA.

---

## Quick Start

### Prerequisites
- Java 17+
- Maven
- SQLite

### Installation
1. Clone the repository:
   ```bash
   git clone https://gitea.kood.tech/sokrad/kmdb.git
   cd movies-api

## Endpoints Overview

### Genres
- **Create Genre**: `POST /api/genres`
- **Get All Genres**: `GET /api/genres`
- **Get Genre by ID**: `GET /api/genres/{id}`
- **Update Genre**: `PATCH /api/genres/{id}`
- **Delete Genre**:
    - Default: `DELETE /api/genres/{id}`
    - Force: `DELETE /api/genres/{id}?force=true`

### Movies
- **Create Movie**: `POST /api/movies`
- **Get All Movies**: `GET /api/movies?page={page}&size={size}`
- **Get Movie by ID**: `GET /api/movies/{id}`
- **Update Movie**: `PATCH /api/movies/{id}`
- **Delete Movie**:
    - Default: `DELETE /api/movies/{id}`
    - Force: `DELETE /api/movies/{id}?force=true`
- **Filter Movies**:
    - By Genre: `GET /api/movies?genre={genreId}`
    - By Release Year: `GET /api/movies?year={releaseYear}`
    - By Actor: `GET /api/movies?actor={actorId}`
- **Search Movies**: `GET /api/movies/search?title={title}`

### Actors
- **Create Actor**: `POST /api/actors`
- **Get All Actors**: `GET /api/actors`
- **Get Actor by ID**: `GET /api/actors/{id}`
- **Update Actor**: `PATCH /api/actors/{id}`
- **Delete Actor**:
    - Default: `DELETE /api/actors/{id}`
    - Force: `DELETE /api/actors/{id}?force=true`
- **Filter Actors**: `GET /api/actors?name={name}`
## Testing the API

Use Postman or any REST client to test the endpoints.  
Delete entities with and without `force=true` to check relationship handling.

## Pagination and Search

Pagination is available on `GET` requests by passing the `page` and `size` parameters, while search for movies is enabled with a case-insensitive partial match on movie titles.

- **Example of a paginated request**:
  ```http
  GET /api/movies?page=0&size=10
  
---

## Notes

- **Pagination**: Available for `GET /api/movies` and `GET /api/actors` using `page` and `size` parameters.
- **Input Validation**: Proper HTTP error codes and messages are returned for invalid inputs.
- **Database**: SQLite is used for easy setup. Check `application.properties` for configurations.

