package com.gearmind.gearmind_app.repository;

import com.gearmind.gearmind_app.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Optional: add custom queries later if needed
}
