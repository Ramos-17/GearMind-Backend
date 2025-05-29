package com.gearmind.gearmind_app.service;

import com.gearmind.gearmind_app.model.Job;
import com.gearmind.gearmind_app.model.JobStage;
import com.gearmind.gearmind_app.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(String customerName, String vehicleInfo) {
        Job job = new Job(customerName, vehicleInfo);
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Optional<Job> advanceJobStage(Long jobId) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);

        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            JobStage current = job.getCurrentStage();

            // Advance to next stage (manually defined)
            switch (current) {
                case ESTIMATE -> job.setCurrentStage(JobStage.TEARDOWN);
                case TEARDOWN -> job.setCurrentStage(JobStage.REPAIR);
                case REPAIR -> job.setCurrentStage(JobStage.PAINT);
                case PAINT -> job.setCurrentStage(JobStage.DETAIL);
                case DETAIL -> job.setCurrentStage(JobStage.COMPLETE);
                case COMPLETE -> throw new RuntimeException("Job is already complete");
            }

            return Optional.of(jobRepository.save(job));
        } else {
            return Optional.empty();
        }
    }
}
