package com.example.sportsapi.service;

import com.example.sportsapi.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class FootballService {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;

    // Function to get league information
    public List<Map<String, Object>> getLeaguesInfo(List<Integer> leagueIds) {
        List<Map<String, Object>> leaguesList = new ArrayList<>();

        for (Integer leagueId : leagueIds) {
            String url = apiConfig.getBaseUrl() + "/leagues";

            // Build the URI with query parameters
            UriComponentsBuilder leagueBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("id", leagueId)
                    .queryParam("season", 2024);

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", apiConfig.getApiKey());
            headers.set("x-rapidapi-host", "v3.football.api-sports.io");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> leagueResponse = restTemplate.exchange(leagueBuilder.toUriString(), HttpMethod.GET, entity, Map.class);

            // Extract the "league" part from the league response
            List<Map<String, Object>> leagueResponseList = (List<Map<String, Object>>) leagueResponse.getBody().get("response");
            if (!leagueResponseList.isEmpty()) {
                Map<String, Object> league = (Map<String, Object>) leagueResponseList.get(0).get("league");
                leaguesList.add(league);  // Add league data to the list
            }
        }

        return leaguesList;
    }

    // Function to get round information for a specific league
    // Used for fetching the current round of fixtures
    public String getRoundInfo(Integer leagueId) {
        String roundUrl = apiConfig.getBaseUrl() + "/fixtures/rounds";

        UriComponentsBuilder roundBuilder = UriComponentsBuilder.fromHttpUrl(roundUrl)
                .queryParam("league", leagueId)
                .queryParam("season", 2024)
                .queryParam("current", "true");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiConfig.getApiKey());
        headers.set("x-rapidapi-host", "v3.football.api-sports.io");

        // Create the HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send the request and get the round response
        ResponseEntity<Map> roundResponse = restTemplate.exchange(roundBuilder.toUriString(), HttpMethod.GET, entity, Map.class);

        // Extract the current round from the round response
        List<String> roundResponseList = (List<String>) roundResponse.getBody().get("response");
        if (!roundResponseList.isEmpty()) {
            return roundResponseList.get(0);  // Return the current round (e.g., "Regular Season - 9")
        }

        return null;  // Return null if no round information is available
    }

    public List<Map<String, Object>> getLeagueStandings(Integer leagueId) {
        List<Map<String, Object>> standingsList = new ArrayList<>();

        String url = apiConfig.getBaseUrl() + "/standings";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("league", leagueId)
                .queryParam("season", 2024);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiConfig.getApiKey());
        headers.set("x-rapidapi-host", "v3.football.api-sports.io");

        // Create the HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            // Send the request and get the standings response
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

            // Extract the "standings" part from the response
            List<Map<String, Object>> standingsResponseList = (List<Map<String, Object>>) response.getBody().get("response");

            if (!standingsResponseList.isEmpty()) {
                Map<String, Object> leagueData = (Map<String, Object>) standingsResponseList.get(0).get("league");

                // Create a map for league information
                Map<String, Object> leagueInfo = new HashMap<>();
                leagueInfo.put("leagueName", leagueData.get("name"));
                leagueInfo.put("leagueLogo", leagueData.get("logo"));

                System.out.println("Response: " + leagueInfo);

                // Add league info as the first item in the standings list
                standingsList.add(leagueInfo);

                List<List<Map<String, Object>>> standings = (List<List<Map<String, Object>>>) leagueData.get("standings");

                // Iterate over standings to extract required data
                for (Map<String, Object> teamData : standings.get(0)) {
                    Map<String, Object> teamStanding = new HashMap<>();

                    teamStanding.put("rank", teamData.get("rank"));

                    Map<String, Object> team = (Map<String, Object>) teamData.get("team");
                    teamStanding.put("teamName", team.get("name"));
                    teamStanding.put("teamLogo", team.get("logo"));

                    Map<String, Object> allStats = (Map<String, Object>) teamData.get("all");
                    teamStanding.put("win", allStats.get("win"));
                    teamStanding.put("draw", allStats.get("draw"));
                    teamStanding.put("lose", allStats.get("lose"));

                    Map<String, Object> goals = (Map<String, Object>) allStats.get("goals");
                    teamStanding.put("goalsFor", goals.get("for"));
                    teamStanding.put("goalsAgainst", goals.get("against"));

                    teamStanding.put("points", teamData.get("points"));
                    System.out.println("Response: " + teamStanding);

                    // Add the team's standing data to the standingsList
                    standingsList.add(teamStanding);
                }
            }
        }
        catch (Exception e) {
                e.printStackTrace(); // Log the exception
                return Collections.emptyList(); // Handle the error appropriately
            }

            return standingsList;
        }
    public List<Map<String, Object>> getLeagueFixture(Integer leagueId) {
        List<Map<String, Object>> fixtureList = new ArrayList<>();

        String round = getRoundInfo(leagueId);  // Get the current round information

        // Build the URI with query parameters // year can be changed
        String url = apiConfig.getBaseUrl() + "/fixtures?league=" + leagueId + "&season=2024&round=" + round;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiConfig.getApiKey());
        headers.set("x-rapidapi-host", "v3.football.api-sports.io");

        // Create the HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            // Send the request and get the fixtures response
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            // System.out.println("Response status: " + response.getStatusCode());
            // System.out.println("Response body: " + response.getBody());
            // Extract the response data
            List<Map<String, Object>> fixtureResponseList = (List<Map<String, Object>>) response.getBody().get("response");
            Map<String, Object> league_response = (Map<String, Object>) fixtureResponseList.get(0).get("league");
            String leagueName = (String) league_response.get("name");
            String leagueLogo = (String) league_response.get("logo");
            Integer leagueSeason = (Integer) league_response.get("season");

            Map<String, Object> league_info = new HashMap<>();

            league_info.put("League Name", leagueName);                // Extract the time
            league_info.put("League Logo", leagueLogo);
            league_info.put("League Season", leagueSeason);

            fixtureList.add(league_info);

            if (!fixtureResponseList.isEmpty()) {
                for (Map<String, Object> fixtureData : fixtureResponseList) {
                    Map<String, Object> fixture = (Map<String, Object>) fixtureData.get("fixture");
                    Map<String, Object> teams = (Map<String, Object>) fixtureData.get("teams");
                    Map<String, Object> goals = (Map<String, Object>) fixtureData.get("goals");

                    // Parse match date and time
                    String matchDate = (String) fixture.get("date");  // "2021-10-22T19:00:00+00:00"
                    String matchTime = matchDate.split("T")[1].split("\\+")[0];  // Extract the time

                    // Parse home and away team names
                    Map<String, Object> homeTeam = (Map<String, Object>) teams.get("home");
                    Map<String, Object> awayTeam = (Map<String, Object>) teams.get("away");

                    String homeTeamName = (String) homeTeam.get("name");
                    String homeTeamLogo = (String) homeTeam.get("logo");
                    String awayTeamName = (String) awayTeam.get("name");
                    String awayTeamLogo = (String) awayTeam.get("logo");

                    // Parse home and away team goals
                    Integer homeScore = (Integer) goals.get("home");
                    Integer awayScore = (Integer) goals.get("away");

                    // Create a map to store the parsed data
                    Map<String, Object> fixtureInfo = new HashMap<>();
                    fixtureInfo.put("date", matchDate.split("T")[0]);  // Extract the date
                    fixtureInfo.put("time", matchTime);                // Extract the time
                    fixtureInfo.put("homeTeam", homeTeamName);
                    fixtureInfo.put("homeTeamLogo", homeTeamLogo);
                    fixtureInfo.put("homeScore", homeScore);
                    fixtureInfo.put("awayTeam", awayTeamName);
                    fixtureInfo.put("awayTeamLogo", awayTeamLogo);
                    fixtureInfo.put("awayScore", awayScore);

                    // Add the parsed fixture information to the list
                    fixtureList.add(fixtureInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fixtureList;
    }
}
