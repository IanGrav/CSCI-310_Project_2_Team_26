package com.example.csci_310project2team26.data.repository;

import android.net.Uri;

import com.example.csci_310project2team26.data.model.Profile;
import com.example.csci_310project2team26.data.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * ProfileRepository - Handles profile data operations
 * Part of the Repository layer in MVVM architecture
 * 
 * Implements UR-2 Profile Creation & Management
 */
public class ProfileRepository {
    
    private final ApiService apiService;
    private final ExecutorService executorService;
    
    public ProfileRepository() {
        this.apiService = ApiService.getInstance();
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Callback interface for async operations
     */
    public interface Callback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
    
    /**
     * Create a new profile for the user
     * 
     * @param userId User ID
     * @param affiliation USC school/department
     * @param birthDate User's birth date
     * @param bio User biography
     * @param interests Optional comma-separated interests
     * @param profileImageUri Optional profile picture URI
     * @param callback Callback for result
     */
    public void createProfile(String userId, String affiliation, String birthDate,
                             String bio, String interests, Uri profileImageUri,
                             Callback<Void> callback) {
        executorService.execute(() -> {
            try {
                // Upload profile image if provided
                String imageUrl = null;
                if (profileImageUri != null) {
                    imageUrl = uploadProfileImage(userId, profileImageUri);
                }
                
                // Create profile
                String token = SessionManager.getToken();
                if (token == null) {
                    callback.onError("Authentication required");
                    return;
                }
                
                Call<Void> call = apiService.createProfile(
                    "Bearer " + token,
                    affiliation,
                    birthDate,
                    bio,
                    interests,
                    imageUrl
                );
                Response<Void> response = call.execute();
                
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    String errorMessage;
                    switch (response.code()) {
                        case 400:
                            errorMessage = "Invalid profile data";
                            break;
                        case 409:
                            errorMessage = "Profile already exists";
                            break;
                        default:
                            errorMessage = "Failed to create profile: " + response.message();
                            break;
                    }
                    callback.onError(errorMessage);
                }
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Network error");
            }
        });
    }
    
    /**
     * Get user profile
     * 
     * @param userId User ID
     * @param callback Callback for result
     */
    public void getProfile(String userId, Callback<Profile> callback) {
        executorService.execute(() -> {
            try {
                Call<Profile> call = apiService.getProfile(userId);
                Response<Profile> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage;
                    switch (response.code()) {
                        case 404:
                            errorMessage = "Profile not found";
                            break;
                        default:
                            errorMessage = "Failed to load profile: " + response.message();
                            break;
                    }
                    callback.onError(errorMessage);
                }
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Network error");
            }
        });
    }
    
    /**
     * Update user profile
     * Note: name, email, and affiliation cannot be changed
     * 
     * @param userId User ID
     * @param birthDate Optional new birth date
     * @param bio Optional new bio
     * @param interests Optional new interests
     * @param profileImageUri Optional new profile picture
     * @param callback Callback for result
     */
    public void updateProfile(String userId, String birthDate, String bio,
                             String interests, Uri profileImageUri,
                             Callback<Void> callback) {
        executorService.execute(() -> {
            try {
                // Upload new profile image if provided
                String imageUrl = null;
                if (profileImageUri != null) {
                    imageUrl = uploadProfileImage(userId, profileImageUri);
                }
                
                // Update profile
                String token = SessionManager.getToken();
                Call<Void> call = apiService.updateProfile(
                    token != null ? ("Bearer " + token) : null,
                    userId,
                    birthDate,
                    bio,
                    interests,
                    imageUrl
                );
                Response<Void> response = call.execute();
                
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to update profile: " + response.message());
                }
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Network error");
            }
        });
    }
    
    /**
     * Reset user password
     * 
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     * @param callback Callback for result
     */
    public void resetPassword(String userId, String currentPassword, 
                             String newPassword, Callback<Void> callback) {
        executorService.execute(() -> {
            try {
                String token = SessionManager.getToken();
                if (token == null) {
                    callback.onError("Authentication required");
                    return;
                }
                
                Call<ApiService.PasswordResetResponse> call = apiService.resetPassword(
                    "Bearer " + token,
                    currentPassword,
                    newPassword
                );
                Response<ApiService.PasswordResetResponse> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(null);
                } else {
                    String errorMessage = "Failed to reset password";
                    
                    // Try to parse error message from response body
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            try {
                                JsonObject errorJson = JsonParser.parseString(errorBody).getAsJsonObject();
                                if (errorJson.has("message")) {
                                    errorMessage = errorJson.get("message").getAsString();
                                } else if (errorJson.has("error")) {
                                    errorMessage = errorJson.get("error").getAsString();
                                }
                            } catch (Exception e) {
                                // If JSON parsing fails, try to extract message manually
                                if (errorBody.contains("\"message\"")) {
                                    int messageIndex = errorBody.indexOf("\"message\":\"");
                                    if (messageIndex >= 0) {
                                        int start = messageIndex + 11;
                                        int end = errorBody.indexOf("\"", start);
                                        if (end > start) {
                                            errorMessage = errorBody.substring(start, end);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // Use default message based on status code
                    }
                    
                    // Fallback to status code-based messages if parsing failed
                    if (errorMessage.equals("Failed to reset password")) {
                        switch (response.code()) {
                            case 401:
                                errorMessage = "Current password is incorrect";
                                break;
                            case 400:
                                errorMessage = "New password does not meet requirements (must be at least 6 characters)";
                                break;
                            case 404:
                                errorMessage = "User not found";
                                break;
                            default:
                                errorMessage = "Failed to reset password. Please try again.";
                                break;
                        }
                    }
                    
                    callback.onError(errorMessage);
                }
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                if (errorMsg == null || errorMsg.isEmpty()) {
                    errorMsg = "Network error. Please check your connection.";
                }
                callback.onError(errorMsg);
            }
        });
    }
    
    /**
     * Upload profile image to server
     * 
     * @param userId User ID
     * @param imageUri Image URI
     * @return Image URL if successful
     */
    private String uploadProfileImage(String userId, Uri imageUri) {
        // This is a placeholder implementation
        // In a real app, you would:
        // 1. Convert URI to File
        // 2. Create MultipartBody.Part for the file
        // 3. Upload to server
        // 4. Return the uploaded image URL
        
        // For now, return a placeholder URL
        return "https://api.bestllm.com/images/" + userId + "/profile.jpg";
    }
}
