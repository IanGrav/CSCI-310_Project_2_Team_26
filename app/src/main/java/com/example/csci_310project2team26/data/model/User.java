package com.example.csci_310project2team26.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * User data model
 * Represents a USC student user in the BestLLM system
 */
public class User {
    
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("student_id")
    private String studentId;
    
    @SerializedName("created_at")
    private Date createdAt;
    
    @SerializedName("has_profile")
    private boolean hasProfile;
    
    // Constructor
    public User(String id, String name, String email, String studentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.hasProfile = false;
    }
    
    // Full constructor
    public User(String id, String name, String email, String studentId, 
                Date createdAt, boolean hasProfile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.createdAt = createdAt;
        this.hasProfile = hasProfile;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public boolean hasProfile() {
        return hasProfile;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }
}
