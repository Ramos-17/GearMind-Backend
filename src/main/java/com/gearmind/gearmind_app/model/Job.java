package com.gearmind.gearmind_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String vehicleInfo;

    @Enumerated(EnumType.STRING)
    private JobStage currentStage;

    private LocalDateTime createdAt;

    // Optional: you can add assigned user, job notes, etc.

    public Job() {}

    public Job(String customerName, String vehicleInfo) {
        this.customerName = customerName;
        this.vehicleInfo = vehicleInfo;
        this.currentStage = JobStage.ESTIMATE;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getCustomerName() { 
        return customerName; 
    }
    
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    public String getVehicleInfo() { 
        return vehicleInfo; 
    }

    public void setVehicleInfo(String vehicleInfo) { 
        this.vehicleInfo = vehicleInfo; 
    }

    public JobStage getCurrentStage() { 
        return currentStage; 
    }

    public void setCurrentStage(JobStage currentStage) { 
        this.currentStage = currentStage; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}
