package com.gearmind.gearmind_app.service;

import com.gearmind.gearmind_app.model.Job;
import com.gearmind.gearmind_app.model.JobHistory;
import com.gearmind.gearmind_app.model.JobStage;
import com.gearmind.gearmind_app.repository.JobRepository;
import com.gearmind.gearmind_app.repository.JobHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    // Create Job (standard CRUD)
    public Job createJob(Job job) {
        // Extract last name from customer name if not already set
        if (job.getCustomerLastName() == null || job.getCustomerLastName().isEmpty()) {
            String customerName = job.getCustomerName();
            if (customerName != null && !customerName.trim().isEmpty()) {
                String[] nameParts = customerName.trim().split("\\s+");
                String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : customerName;
                job.setCustomerLastName(lastName);
            }
        }
        
        // Set creation time if not set
        if (job.getCreatedAt() == null) {
            job.setCreatedAt(LocalDateTime.now());
        }
        
        Job savedJob = jobRepository.save(job);
        
        // Log job creation
        JobHistory history = new JobHistory(savedJob, "CREATE", "job", "", "Job created", "SYSTEM");
        jobHistoryRepository.save(history);
        
        return savedJob;
    }

    // Get all Jobs (excluding archived by default)
    public List<Job> getAllJobs() {
        try {
            List<Job> jobs = jobRepository.findByArchivedFalse();
            // Ensure all jobs have a valid stage
            jobs.forEach(job -> {
                if (job.getCurrentStage() == null) {
                    job.setCurrentStage(JobStage.ESTIMATE);
                }
            });
            return jobs;
        } catch (Exception e) {
            // Fallback: get all jobs and filter manually
            List<Job> allJobs = jobRepository.findAll();
            return allJobs.stream()
                .filter(job -> !job.isArchived())
                .peek(job -> {
                    if (job.getCurrentStage() == null) {
                        job.setCurrentStage(JobStage.ESTIMATE);
                    }
                })
                .toList();
        }
    }
    
    
    public List<Job> getAllJobsIncludingArchived() {
        List<Job> allJobs = jobRepository.findAll();
        return allJobs;
    }
    
    
    public List<Job> getArchivedJobs() {
        try {
            List<Job> jobs = jobRepository.findByArchivedTrue();
            
            return jobs.stream()
                .filter(job -> job.getCurrentStage() == JobStage.COMPLETE)
                .toList();
        } catch (Exception e) {
            
            List<Job> allJobs = jobRepository.findAll();
            return allJobs.stream()
                .filter(job -> job.isArchived() && job.getCurrentStage() == JobStage.COMPLETE)
                .toList();
        }
    }

    
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    
    public Job updateJobStage(Long id, JobStage newStage, String updatedBy) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        JobStage oldStage = job.getCurrentStage();
        job.setCurrentStage(newStage);
        job.setLastUpdated(LocalDateTime.now());
        
        Job savedJob = jobRepository.save(job);
        
        
        String oldStageStr = oldStage != null ? oldStage.toString() : "NULL";
        JobHistory history = new JobHistory(savedJob, "UPDATE", "stage", oldStageStr, newStage.toString(), updatedBy);
        jobHistoryRepository.save(history);
        
        return savedJob;
    }
    
    
    public Job updateJob(Long id, Job updatedJob, String updatedBy) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        
        if (!Objects.equals(existingJob.getCustomerName(), updatedJob.getCustomerName())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "customerName", 
                existingJob.getCustomerName(), updatedJob.getCustomerName(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getVehicleInfo(), updatedJob.getVehicleInfo())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "vehicleInfo", 
                existingJob.getVehicleInfo(), updatedJob.getVehicleInfo(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getCurrentStage(), updatedJob.getCurrentStage())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "currentStage", 
                existingJob.getCurrentStage() != null ? existingJob.getCurrentStage().toString() : "null", 
                updatedJob.getCurrentStage() != null ? updatedJob.getCurrentStage().toString() : "null", 
                updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getAssignedTechnician(), updatedJob.getAssignedTechnician())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "assignedTechnician", 
                existingJob.getAssignedTechnician(), updatedJob.getAssignedTechnician(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getJobNotes(), updatedJob.getJobNotes())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "jobNotes", 
                existingJob.getJobNotes(), updatedJob.getJobNotes(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        
        if (!Objects.equals(existingJob.getPaintColor(), updatedJob.getPaintColor())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "paintColor", 
                existingJob.getPaintColor(), updatedJob.getPaintColor(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getPaintFinish(), updatedJob.getPaintFinish())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "paintFinish", 
                existingJob.getPaintFinish(), updatedJob.getPaintFinish(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getPaintCode(), updatedJob.getPaintCode())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "paintCode", 
                existingJob.getPaintCode(), updatedJob.getPaintCode(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getPaintUsed(), updatedJob.getPaintUsed())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "paintUsed", 
                existingJob.getPaintUsed(), updatedJob.getPaintUsed(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getMaskingUsed(), updatedJob.getMaskingUsed())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "maskingUsed", 
                existingJob.getMaskingUsed(), updatedJob.getMaskingUsed(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getPrepNotes(), updatedJob.getPrepNotes())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "prepNotes", 
                existingJob.getPrepNotes(), updatedJob.getPrepNotes(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        
        if (!Objects.equals(existingJob.getQualityRating(), updatedJob.getQualityRating())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "qualityRating", 
                existingJob.getQualityRating(), updatedJob.getQualityRating(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getInspectionNotes(), updatedJob.getInspectionNotes())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "inspectionNotes", 
                existingJob.getInspectionNotes(), updatedJob.getInspectionNotes(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getPickupInstructions(), updatedJob.getPickupInstructions())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "pickupInstructions", 
                existingJob.getPickupInstructions(), updatedJob.getPickupInstructions(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getCustomerContact(), updatedJob.getCustomerContact())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "customerContact", 
                existingJob.getCustomerContact(), updatedJob.getCustomerContact(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        if (!Objects.equals(existingJob.getChecklistItems(), updatedJob.getChecklistItems())) {
            JobHistory history = new JobHistory(existingJob, "UPDATE", "checklistItems", 
                existingJob.getChecklistItems(), updatedJob.getChecklistItems(), updatedBy);
            jobHistoryRepository.save(history);
        }
        
        
        existingJob.setCustomerName(updatedJob.getCustomerName());

        if (updatedJob.getCustomerName() != null && !updatedJob.getCustomerName().trim().isEmpty()) {
            String[] nameParts = updatedJob.getCustomerName().trim().split("\\s+");
            String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : updatedJob.getCustomerName();
            existingJob.setCustomerLastName(lastName);
        }
        existingJob.setVehicleInfo(updatedJob.getVehicleInfo());
        existingJob.setCurrentStage(updatedJob.getCurrentStage());
        existingJob.setAssignedTechnician(updatedJob.getAssignedTechnician());
        existingJob.setJobNotes(updatedJob.getJobNotes());
        existingJob.setPaintColor(updatedJob.getPaintColor());
        existingJob.setPaintFinish(updatedJob.getPaintFinish());
        existingJob.setPaintCode(updatedJob.getPaintCode());
        existingJob.setPaintUsed(updatedJob.getPaintUsed());
        existingJob.setMaskingUsed(updatedJob.getMaskingUsed());
        existingJob.setPrepNotes(updatedJob.getPrepNotes());
        existingJob.setQualityRating(updatedJob.getQualityRating());
        existingJob.setInspectionNotes(updatedJob.getInspectionNotes());
        existingJob.setPickupInstructions(updatedJob.getPickupInstructions());
        existingJob.setCustomerContact(updatedJob.getCustomerContact());
        existingJob.setChecklistItems(updatedJob.getChecklistItems());
        existingJob.setLastUpdated(LocalDateTime.now());
        
        return jobRepository.save(existingJob);
    }
    
    
    public Job archiveJob(Long id, String archivedBy) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        
        if (job.getCurrentStage() != JobStage.COMPLETE) {
            throw new RuntimeException("Only completed jobs can be archived. Current stage: " + job.getCurrentStage());
        }
        
        job.setArchived(true);
        job.setLastUpdated(LocalDateTime.now());
        
        Job savedJob = jobRepository.save(job);
        
        // Log archiving
        JobHistory history = new JobHistory(savedJob, "ARCHIVE", "archived", "false", "true", archivedBy);
        jobHistoryRepository.save(history);
        
        return savedJob;
    }
    
    // Unarchive a job
    public Job unarchiveJob(Long id, String unarchivedBy) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        job.setArchived(false);
        job.setLastUpdated(LocalDateTime.now());
        
        Job savedJob = jobRepository.save(job);
        
        // Log unarchiving
        JobHistory history = new JobHistory(savedJob, "UNARCHIVE", "archived", "true", "false", unarchivedBy);
        jobHistoryRepository.save(history);
        
        return savedJob;
    }
    
    // Get job history
    public List<JobHistory> getJobHistory(Long jobId) {
        return jobHistoryRepository.findByJobIdOrderByTimestampDesc(jobId);
    }

    // Delete Job
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // First, delete all job history records for this job to avoid foreign key constraint violations
        List<JobHistory> jobHistories = jobHistoryRepository.findByJobIdOrderByTimestampDesc(id);
        jobHistoryRepository.deleteAll(jobHistories);
        
        // Now delete the job
        jobRepository.deleteById(id);
    }

    public List<Job> getJobsByCustomerLastName(String lastName) {
        // First try to find by customerLastName
        List<Job> jobsByLastName = jobRepository.findByCustomerLastNameIgnoreCase(lastName);
        if (!jobsByLastName.isEmpty()) {
            // Filter out archived jobs and ensure valid stages
            return jobsByLastName.stream()
                .filter(job -> !job.isArchived())
                .peek(job -> {
                    if (job.getCurrentStage() == null) {
                        job.setCurrentStage(JobStage.ESTIMATE);
                    }
                })
                .toList();
        }
        
        // Fallback: search in customerName field
        List<Job> jobsByName = jobRepository.findByCustomerNameContainingIgnoreCase(lastName);
        return jobsByName.stream()
            .filter(job -> !job.isArchived())
            .peek(job -> {
                if (job.getCurrentStage() == null) {
                    job.setCurrentStage(JobStage.ESTIMATE);
                }
            })
            .toList();
    }

    public Job updateJobNotes(Long id, String newNotes, String updatedBy) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        String oldNotes = job.getJobNotes();
        job.setJobNotes(newNotes);
        job.setLastUpdated(LocalDateTime.now());
        
        Job savedJob = jobRepository.save(job);
        
        // Log notes update
        JobHistory history = new JobHistory(savedJob, "UPDATE", "jobNotes", oldNotes, newNotes, updatedBy);
        jobHistoryRepository.save(history);
        
        return savedJob;
    }
}