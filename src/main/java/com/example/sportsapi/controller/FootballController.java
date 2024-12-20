package com.example.sportsapi.controller;

import com.example.sportsapi.config.ApiConfig;
import com.example.sportsapi.service.FootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/football")
public class FootballController {


    @Autowired
    private ApiConfig apiConfig;
    @Autowired
    private FootballService footballService;

    @GetMapping("/leagues")
    public ResponseEntity<List<Map<String, Object>>> getLeaguesInfo() {
        // List of league IDs from your table
        List<Integer> leagueIds = apiConfig.getleagueIds();

        // Fetch the league data
        List<Map<String, Object>> leagueInfo = footballService.getLeaguesInfo(leagueIds);
        return ResponseEntity.ok(leagueInfo);
    }
    @GetMapping("/standings/{leagueId}")
    public ResponseEntity<List<Map<String, Object>>> getStandings(@PathVariable Integer leagueId) {
        List<Map<String, Object>> leagueInfo = footballService.getLeagueStandings(leagueId);
        return ResponseEntity.ok(leagueInfo);
    }


    @GetMapping("/fixtures/{leagueId}")
    public ResponseEntity<List<Map<String, Object>>> getFixture(@PathVariable Integer leagueId) {

        List<Map<String, Object>> leagueInfo = footballService.getLeagueFixture(leagueId);
        return ResponseEntity.ok(leagueInfo);
    }
}

