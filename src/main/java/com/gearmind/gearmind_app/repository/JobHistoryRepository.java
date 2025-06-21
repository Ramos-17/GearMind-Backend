package com.gearmind.gearmind_app.repository;

import com.gearmind.gearmind_app.model.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    List<JobHistory> findByJobIdOrderByTimestampDesc(Long jobId);
} 