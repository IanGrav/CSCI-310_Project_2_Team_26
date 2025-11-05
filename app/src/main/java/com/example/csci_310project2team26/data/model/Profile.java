package com.example.csci_310project2team26.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Profile data model
 * Requirements: UR-2 Profile Creation & Management
 * 
 * Fields:
 * - name, email, affiliation: Cannot be changed after creation
 * - birthDate, bio, interests, profilePictureUrl: Can be updated
 * - Password can be reset through separate API
 */
public class Profile {
    
    @SerializedName("user_id")
    private String userId;
    
    // Non-editable fields
    @SerializedName("name")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("affiliation")
    private String affiliation;
    
    // Editable fields
    @SerializedName("birth_date")
    private String birthDate;
    
    @SerializedName("bio")
    private String bio;
    
    @SerializedName("interests")
    private String interests;
    
    @SerializedName("profile_picture_url")
    private String profilePictureUrl;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Constructor
    public Profile(String userId, String name, String email, String affiliation,
                   String birthDate, String bio) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.affiliation = affiliation;
        this.birthDate = birthDate;
        this.bio = bio;
    }
    
    // Full constructor
    public Profile(String userId, String name, String email, String affiliation,
                   String birthDate, String bio, String interests, String profilePictureUrl,
                   String createdAt, String updatedAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.affiliation = affiliation;
        this.birthDate = birthDate;
        this.bio = bio;
        this.interests = interests;
        this.profilePictureUrl = profilePictureUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getAffiliation() {
        return affiliation;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    
    public String getBio() {
        return bio;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters for editable fields only
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
