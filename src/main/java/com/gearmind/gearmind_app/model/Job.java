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
    @Column(name = "customer_last_name")
    private String customerLastName;
    private String vehicleInfo;

    @Enumerated(EnumType.STRING)
    private JobStage currentStage;

    private LocalDateTime createdAt;

    private String assignedTechnician;
    private String jobNotes;
    private LocalDateTime lastUpdated;
    
    // Paint-specific fields
    private String paintColor;
    private String paintFinish;
    private String paintCode;
    private String paintUsed;
    private String maskingUsed;
    private String prepNotes;
    
    // Detail-specific fields
    private String qualityRating;
    private String inspectionNotes;
    private String pickupInstructions;
    private String customerContact;
    private String checklistItems;
    
    private boolean archived = false;

    // Default constructor for JPA
    public Job() {
    }

    // Custom constructor
    public Job(String customerName, String vehicleInfo) {
        this.customerName = customerName;
        this.vehicleInfo = vehicleInfo;
        this.currentStage = JobStage.ESTIMATE;
        this.createdAt = LocalDateTime.now();
        this.assignedTechnician = "";
        this.jobNotes = "";
        this.archived = false;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
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

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public String getJobNotes() {
        return jobNotes;
    }
    
    public void setJobNotes(String jobNotes) {
        this.jobNotes = jobNotes;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public boolean isArchived() {
        return archived;
    }
    
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(String paintColor) {
        this.paintColor = paintColor;
    }

    public String getPaintFinish() {
        return paintFinish;
    }

    public void setPaintFinish(String paintFinish) {
        this.paintFinish = paintFinish;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getPaintUsed() {
        return paintUsed;
    }

    public void setPaintUsed(String paintUsed) {
        this.paintUsed = paintUsed;
    }

    public String getMaskingUsed() {
        return maskingUsed;
    }

    public void setMaskingUsed(String maskingUsed) {
        this.maskingUsed = maskingUsed;
    }

    public String getPrepNotes() {
        return prepNotes;
    }

    public void setPrepNotes(String prepNotes) {
        this.prepNotes = prepNotes;
    }

    public String getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(String qualityRating) {
        this.qualityRating = qualityRating;
    }

    public String getInspectionNotes() {
        return inspectionNotes;
    }

    public void setInspectionNotes(String inspectionNotes) {
        this.inspectionNotes = inspectionNotes;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(String checklistItems) {
        this.checklistItems = checklistItems;
    }
}