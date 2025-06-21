package com.gearmind.gearmind_app.repository;

import com.gearmind.gearmind_app.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCustomerLastNameIgnoreCase(String lastName);
    List<Job> findByCustomerNameContainingIgnoreCase(String lastName);
    List<Job> findByArchivedFalse();
    List<Job> findByArchivedTrue();
}
