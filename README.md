# Sports API

This project is a Spring Boot application that provides an API for retrieving sports data, including league standings and fixtures.

## Features

- Retrieve league standings
- Retrieve league fixtures
- Externalized configuration using `@ConfigurationProperties`

## Technologies Used

- Java
- Spring Boot
- Maven
- RESTful API

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/omeryldzk/sportsapi.git
    cd sportsapi
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Configuration

Configure the API properties in `application.properties` or `application.yml`:

```properties
api.sports.base-url=https://api.example.com
api.sports.key=your-api-key
api.sports.league-ids=1,2,3
```

### Endpoints

- **Get Leagues Info**
    ```http
    GET /api/football/leagues
    ```
    Retrieves information about leagues.

- **Get League Standings**
    ```http
    GET /api/football/standings/{leagueId}
    ```
    Retrieves the standings for a specific league.

- **Get League Fixtures**
    ```http
    GET /api/football/fixtures/{leagueId}
    ```
    Retrieves the fixtures for a specific league.

### Example Requests

- **Get Leagues Info**
    ```sh
    curl -X GET "http://localhost:8080/api/football/leagues"
    ```

- **Get League Standings**
    ```sh
    curl -X GET "http://localhost:8080/api/football/standings/1"
    ```

- **Get League Fixtures**
    ```sh
