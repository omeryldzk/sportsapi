package com.example.sportsapi.controller;

import com.example.sportsapi.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/worker")
class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerWorker() {
        workerService.fetchAndStoreLeagues();
        return ResponseEntity.ok("Worker triggered successfully");
    }
}
