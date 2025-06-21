package com.gearmind.gearmind_app.controller;

import com.gearmind.gearmind_app.model.Job;
import com.gearmind.gearmind_app.model.JobHistory;
import com.gearmind.gearmind_app.model.JobStage;
import com.gearmind.gearmind_app.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.gearmind.gearmind_app.repository.JobRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173") // Adjust port if needed
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

   
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

   
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public List<Job> getAllJobs() {
        // Debug logging
        System.out.println("📋 Get all jobs request");
        System.out.println("🔐 Current authentication: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("🔐 Current authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println("🔐 Current principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        return jobService.getAllJobs();
    }
    
    
    @GetMapping("/all-including-archived")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<Job> getAllJobsIncludingArchived() {
        return jobService.getAllJobsIncludingArchived();
    }
    
   
    @GetMapping("/archived")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<Job> getArchivedJobs() {
        return jobService.getArchivedJobs();
    }

    
    @GetMapping("/search")
    public List<Job> getJobsByLastName(@RequestParam String lastName) {
        return jobService.getJobsByCustomerLastName(lastName);
    }

    
    @GetMapping("/public")
    public List<Job> getPublicJobs() {
        return jobService.getAllJobs();
    }

    
    @PutMapping("/{id}/update-stage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job updateJobStage(@PathVariable Long id, @RequestBody String newStage) {
        return jobService.updateJobStage(id, JobStage.valueOf(newStage), "MANAGER");
    }
    
    
    @PutMapping("/{id}/update-stage-with-user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job updateJobStageWithUser(@PathVariable Long id, @RequestBody StageUpdateRequest request) {
        return jobService.updateJobStage(id, JobStage.valueOf(request.getNewStage()), request.getUpdatedBy());
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job updateJob(@PathVariable Long id, @RequestBody JobUpdateRequest request) {
        return jobService.updateJob(id, request.getJob(), request.getUpdatedBy());
    }

   
    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public Job archiveJob(@PathVariable Long id, @RequestBody ArchiveRequest request) {
        return jobService.archiveJob(id, request.getArchivedBy());
    }
    
    
    @PutMapping("/{id}/unarchive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public Job unarchiveJob(@PathVariable Long id, @RequestBody ArchiveRequest request) {
        return jobService.unarchiveJob(id, request.getArchivedBy());
    }
    
   
    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<JobHistory> getJobHistory(@PathVariable Long id) {
        return jobService.getJobHistory(id);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public void deleteJob(@PathVariable Long id) {
        // Debug logging
        System.out.println("🗑️ Delete job request for ID: " + id);
        System.out.println("🔐 Current authentication: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("🔐 Current authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        
        jobService.deleteJob(id);
    }

   
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id).orElse(null);
    }

    @PutMapping("/{id}/update-notes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job updateJobNotes(@PathVariable Long id, @RequestBody String newNotes) {
        return jobService.updateJobNotes(id, newNotes, "MANAGER");
    }
    
    
    @PutMapping("/{id}/update-notes-with-user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_TECH', 'ROLE_PAINTER', 'ROLE_DETAILER')")
    public Job updateJobNotesWithUser(@PathVariable Long id, @RequestBody NotesUpdateRequest request) {
        return jobService.updateJobNotes(id, request.getNewNotes(), request.getUpdatedBy());
    }
    
    // Request classes for better API structure
    public static class StageUpdateRequest {
        private String newStage;
        private String updatedBy;
        
        public String getNewStage() { return newStage; }
        public void setNewStage(String newStage) { this.newStage = newStage; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    }
    
    public static class JobUpdateRequest {
        private Job job;
        private String updatedBy;
        
        public Job getJob() { return job; }
        public void setJob(Job job) { this.job = job; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    }
    
    public static class ArchiveRequest {
        private String archivedBy;
        
        public String getArchivedBy() { return archivedBy; }
        public void setArchivedBy(String archivedBy) { this.archivedBy = archivedBy; }
    }
    
    public static class NotesUpdateRequest {
        private String newNotes;
        private String updatedBy;
        
        public String getNewNotes() { return newNotes; }
        public void setNewNotes(String newNotes) { this.newNotes = newNotes; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    }
}
