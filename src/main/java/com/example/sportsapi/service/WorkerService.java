package com.example.sportsapi.service;

import com.example.sportsapi.config.ApiConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class WorkerService {

    private final Storage storage;
    private final FootballService footballService;
    private final ApiConfig apiConfig;

    // Constructor-based injection
    @Autowired
    public WorkerService(FootballService footballService, ApiConfig apiConfig) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.footballService = footballService;
        this.apiConfig = apiConfig;
    }


    public void fetchAndStoreLeagues() {
        List<Map<String, Object>> leagues = fetchLeagues();

        for (Map<String, Object> league : leagues) {
            String leagueName = ((String) league.get("name")).toLowerCase().replace(" ", "-");
            int leagueId = Integer.parseInt(String.valueOf(league.get("id")));
            List<Map<String, Object>> leagueStandings = footballService.getLeagueStandings(leagueId);
            List<Map<String, Object>> leagueFixtures = footballService.getLeagueFixture(leagueId);
            uploadToGcs(apiConfig.getBucketName(), leagueName + "-standings.json", leagueStandings.toString());
            uploadToGcs(apiConfig.getBucketName(), leagueName + "-fixtures.json", leagueFixtures.toString());
        }
    }
    private List<Map<String, Object>> fetchLeagues() {
        // List of league IDs from your table
        List<Integer> leagueIds = apiConfig.getleagueIds();
        // Fetch the league data
        List<Map<String, Object>> leagueInfo = footballService.getLeaguesInfo(leagueIds);
        return leagueInfo;
    }

    private void uploadToGcs(String bucketName, String objectName, String data) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/json").build();
        storage.create(blobInfo, data.getBytes(StandardCharsets.UTF_8));
    }
}

