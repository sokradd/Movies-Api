
# Movies API

## Description

**Movies API** is a REST API built with Spring Boot and JPA for managing a movie database, including movies, genres, and actors. This API allows users to perform CRUD operations (Create, Read, Update, Delete) for movies, genres, and actors. The API also supports filtering movies by genre, release year, and actors.

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **SQLite** (Database)
- **Gradle** (Build tool)

## API Features

1. Manage movies (CRUD operations).
2. Manage genres (CRUD operations).
3. Manage actors (CRUD operations).
4. Filter movies by genre, release year, and actors.
5. Pagination for movie lists.
6. Search movies by title.

## Installation and Setup

### 1. Prerequisites

Ensure that you have the following installed:

- **Java 21** or newer
- **Gradle**
- **SQLite**

### 2. Cloning the Project

Clone the repository to your local machine:

```bash
git clone https://github.com/your-username/movies-api.git
cd movies-api
```

### 3. Project Setup

After cloning the repository, follow these steps:

1. **Database Configuration:**
   This project uses SQLite as the database. You can modify the configuration in the `src/main/resources/application.properties` file if you wish to use a different database.

   Example configuration for SQLite:
   ```properties
   spring.datasource.url=jdbc:sqlite:movies.db
   spring.datasource.driver-class-name=org.sqlite.JDBC
   spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
   spring.jpa.show-sql=true
   spring.jpa.hibernate.ddl-auto=update
   ```

2. **Install Dependencies:**
   Install all dependencies using Gradle:

   ```bash
   ./gradlew build
   ```

### 4. Running the Application

Run the application using Gradle:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.

## API Usage

### Main Endpoints

#### 1. Movies

- **Create a Movie:**
  ```bash
  POST /api/movies
  ```
  Example request body:
  ```json
  {
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [1, 2],  // IDs of genres
    "actors": [1, 2, 3]  // IDs of actors
  }
  ```

- **Get All Movies:**
  ```bash
  GET /api/movies
  ```

- **Get Movie by ID:**
  ```bash
  GET /api/movies/{id}
  ```

- **Update Movie (Partial):**
  ```bash
  PATCH /api/movies/{id}
  ```

- **Delete Movie:**
  ```bash
  DELETE /api/movies/{id}
  ```

- **Filter Movies by Genre:**
  ```bash
  GET /api/movies?genre={genreId}
  ```

- **Filter Movies by Release Year:**
  ```bash
  GET /api/movies?year={releaseYear}
  ```

- **Search Movies by Title:**
  ```bash
  GET /api/movies/search?title={title}
  ```

#### 2. Genres

- **Create a Genre:**
  ```bash
  POST /api/genres
  ```
  Example request body:
  ```json
  {
    "name": "Action"
  }
  ```

- **Get All Genres:**
  ```bash
  GET /api/genres
  ```

- **Get Genre by ID:**
  ```bash
  GET /api/genres/{id}
  ```

- **Update Genre (Partial):**
  ```bash
  PATCH /api/genres/{id}
  ```

- **Delete Genre:**
  ```bash
  DELETE /api/genres/{id}
  ```

#### 3. Actors

- **Create an Actor:**
  ```bash
  POST /api/actors
  ```
  Example request body:
  ```json
  {
    "name": "Leonardo DiCaprio",
    "birthDate": "1974-11-11"
  }
  ```

- **Get All Actors:**
  ```bash
  GET /api/actors
  ```

- **Get Actor by ID:**
  ```bash
  GET /api/actors/{id}
  ```

- **Update Actor (Partial):**
  ```bash
  PATCH /api/actors/{id}
  ```

- **Delete Actor:**
  ```bash
  DELETE /api/actors/{id}
  ```

## Testing

You can test the API using **Postman** or any other HTTP client.

### Example Postman Requests

1. Create a collection in Postman with requests for each endpoint (`POST`, `GET`, `PATCH`, `DELETE`).
2. For `POST` and `PATCH` requests, add the JSON request body.

### Error Handling

- **404 Not Found**: When a requested resource is not found.
- **400 Bad Request**: If there are validation errors in the request data.
- **500 Internal Server Error**: In case of a server-side error.

## Additional Features

- **Pagination**: Add `page` and `size` parameters to the request for paginated movie lists.
  Example:
  ```bash
  GET /api/movies?page=0&size=10
  ```

## License

This project is licensed under the MIT License. You are free to use, modify, and distribute the code as you wish.
