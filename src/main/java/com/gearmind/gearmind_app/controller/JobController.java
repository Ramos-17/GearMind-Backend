package com.gearmind.gearmind_app.controller;

import com.gearmind.gearmind_app.model.Job;
import com.gearmind.gearmind_app.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Create a new job
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job jobRequest) {
        Job job = jobService.createJob(jobRequest.getCustomerName(), jobRequest.getVehicleInfo());
        return ResponseEntity.ok(job);
    }

    // Get all jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // Advance a job to the next stage
    @PutMapping("/{id}/advance")
    public ResponseEntity<?> advanceJob(@PathVariable Long id) {
        Optional<Job> updated = jobService.advanceJobStage(id);
        return updated.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get a job by ID (optional)
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobService.getJobById(id);
        return job.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
