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
    curl -X GET "http://localhost:8080/api/football/fixtures/1"
    ```

### Example Responses

#### Get Leagues Info
**Request:**
```sh
curl -X GET "http://localhost:8080/api/football/leagues"
```
**Response:**
```json
[
    {
        "leagueName": "Premier League",
        "leagueLogo": "https://example.com/logo.png"
    },
    {
        "leagueName": "La Liga",
        "leagueLogo": "https://example.com/logo.png"
    }
]
```

#### Get League Standings
**Request:**
```sh
curl -X GET "http://localhost:8080/api/football/standings/1"
```
**Response:**
```json
[
    {
        "leagueName": "Premier League",
        "leagueLogo": "https://example.com/logo.png"
    },
    {
        "rank": 1,
        "teamName": "Team A",
        "teamLogo": "https://example.com/teamA.png",
        "win": 10,
        "draw": 2,
        "lose": 1,
        "goalsFor": 30,
        "goalsAgainst": 10,
        "points": 32
    }
]
```

#### Get League Fixtures
**Request:**
```sh
curl -X GET "http://localhost:8080/api/football/fixtures/1"
```
**Response:**
```json
[
    {
        "League Name": "Premier League",
        "League Logo": "https://example.com/logo.png",
        "League Season": 2024
    },
    {
        "date": "2024-10-22",
        "time": "19:00:00",
        "homeTeam": "Team A",
        "homeTeamLogo": "https://example.com/teamA.png",
        "homeScore": 2,
        "awayTeam": "Team B",
        "awayTeamLogo": "https://example.com/teamB.png",
        "awayScore": 1
    }
]
```

### Explanation of Methods

#### `getLeagueStandings`
This method retrieves the standings for a specific league. It constructs a URL with the league ID and season, sets the necessary headers, and sends a GET request to the API. The response is parsed to extract league and team standings information, which is then returned as a list of maps.

#### `getLeagueFixture`
This method retrieves the fixtures for a specific league. It first gets the current round information, constructs a URL with the league ID, season, and round, sets the necessary headers, and sends a GET request to the API. The response is parsed to extract fixture information, which is then returned as a list of maps.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
