package com.example.csci_310project2team26.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csci_310project2team26.data.model.Profile;
import com.example.csci_310project2team26.data.repository.ProfileRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ProfileViewModel - Handles profile management business logic
 * Implements UR-2 Profile Creation & Management
 */
public class ProfileViewModel extends ViewModel {
    
    private final ProfileRepository profileRepository;
    private final ExecutorService executorService;
    
    // Profile Creation State
    private final MutableLiveData<ProfileCreationState> profileCreationState = new MutableLiveData<>();
    
    // Profile Update State
    private final MutableLiveData<ProfileUpdateState> profileUpdateState = new MutableLiveData<>();
    
    // Current Profile
    private final MutableLiveData<Profile> currentProfile = new MutableLiveData<>();
    
    public ProfileViewModel() {
        this.profileRepository = new ProfileRepository();
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    // State classes
    public static abstract class ProfileCreationState {
        public static class Idle extends ProfileCreationState {}
        public static class Loading extends ProfileCreationState {}
        public static class Success extends ProfileCreationState {}
        public static class Error extends ProfileCreationState {
            private final String message;
            public Error(String message) {
                this.message = message;
            }
            public String getMessage() {
                return message;
            }
        }
    }
    
    public static abstract class ProfileUpdateState {
        public static class Idle extends ProfileUpdateState {}
        public static class Loading extends ProfileUpdateState {}
        public static class Success extends ProfileUpdateState {}
        public static class Error extends ProfileUpdateState {
            private final String message;
            public Error(String message) {
                this.message = message;
            }
            public String getMessage() {
                return message;
            }
        }
    }
    
    public LiveData<ProfileCreationState> getProfileCreationState() {
        return profileCreationState;
    }
    
    public LiveData<ProfileUpdateState> getProfileUpdateState() {
        return profileUpdateState;
    }
    
    public LiveData<Profile> getCurrentProfile() {
        return currentProfile;
    }
    
    /**
     * Create initial profile for new user
     * Required fields: affiliation, birthDate, bio
     * Optional fields: interests, profileImageUri
     */
    public void createProfile(String userId, String affiliation, String birthDate, 
                             String bio, String interests, Uri profileImageUri) {
        profileCreationState.postValue(new ProfileCreationState.Loading());
        
        executorService.execute(() -> {
            try {
                // Validate required fields
                if (affiliation == null || affiliation.isEmpty() || 
                    birthDate == null || birthDate.isEmpty() || 
                    bio == null || bio.isEmpty()) {
                    profileCreationState.postValue(new ProfileCreationState.Error(
                        "Please fill in all required fields"
                    ));
                    return;
                }
                
                // Validate bio length
                if (bio.length() < 10) {
                    profileCreationState.postValue(new ProfileCreationState.Error(
                        "Bio must be at least 10 characters"
                    ));
                    return;
                }
                
                // Call repository to create profile
                profileRepository.createProfile(
                    userId, 
                    affiliation, 
                    birthDate, 
                    bio, 
                    interests, 
                    profileImageUri,
                    new ProfileRepository.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            profileCreationState.postValue(new ProfileCreationState.Success());
                        }
                        
                        @Override
                        public void onError(String error) {
                            profileCreationState.postValue(new ProfileCreationState.Error(error));
                        }
                    }
                );
                
            } catch (Exception e) {
                profileCreationState.postValue(new ProfileCreationState.Error(
                    e.getMessage() != null ? e.getMessage() : "An unexpected error occurred"
                ));
            }
        });
    }
    
    /**
     * Load user profile
     */
    public void loadProfile(String userId) {
        executorService.execute(() -> {
            try {
                profileRepository.getProfile(userId, new ProfileRepository.Callback<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        currentProfile.postValue(profile);
                    }
                    
                    @Override
                    public void onError(String error) {
                        // Handle error silently or log
                    }
                });
            } catch (Exception e) {
                // Handle error silently or log
            }
        });
    }
    
    /**
     * Update profile
     * Note: Name, email, and affiliation cannot be changed per UR-2
     */
    public void updateProfile(String userId, String birthDate, String bio, 
                            String interests, Uri profileImageUri) {
        profileUpdateState.postValue(new ProfileUpdateState.Loading());
        
        executorService.execute(() -> {
            try {
                // Validate bio if provided
                if (bio != null && bio.length() < 10) {
                    profileUpdateState.postValue(new ProfileUpdateState.Error(
                        "Bio must be at least 10 characters"
                    ));
                    return;
                }
                
                // Call repository to update profile
                profileRepository.updateProfile(
                    userId, 
                    birthDate, 
                    bio, 
                    interests, 
                    profileImageUri,
                    new ProfileRepository.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            profileUpdateState.postValue(new ProfileUpdateState.Success());
                            // Reload profile to get updated data
                            loadProfile(userId);
                        }
                        
                        @Override
                        public void onError(String error) {
                            profileUpdateState.postValue(new ProfileUpdateState.Error(error));
                        }
                    }
                );
                
            } catch (Exception e) {
                profileUpdateState.postValue(new ProfileUpdateState.Error(
                    e.getMessage() != null ? e.getMessage() : "An unexpected error occurred"
                ));
            }
        });
    }
    
    /**
     * Reset password
     */
    public void resetPassword(String userId, String currentPassword, String newPassword) {
        profileUpdateState.postValue(new ProfileUpdateState.Loading());
        
        executorService.execute(() -> {
            try {
                // Validate inputs
                if (currentPassword == null || currentPassword.trim().isEmpty()) {
                    profileUpdateState.postValue(new ProfileUpdateState.Error(
                        "Current password is required"
                    ));
                    return;
                }
                
                if (newPassword == null || newPassword.trim().isEmpty()) {
                    profileUpdateState.postValue(new ProfileUpdateState.Error(
                        "New password is required"
                    ));
                    return;
                }
                
                // Validate new password length (backend requires 6, but we'll use 6 to match)
                if (newPassword.length() < 6) {
                    profileUpdateState.postValue(new ProfileUpdateState.Error(
                        "New password must be at least 6 characters"
                    ));
                    return;
                }
                
                profileRepository.resetPassword(
                    userId, 
                    currentPassword.trim(), 
                    newPassword.trim(),
                    new ProfileRepository.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            profileUpdateState.postValue(new ProfileUpdateState.Success());
                        }
                        
                        @Override
                        public void onError(String error) {
                            profileUpdateState.postValue(new ProfileUpdateState.Error(
                                error != null ? error : "Failed to reset password"
                            ));
                        }
                    }
                );
                
            } catch (Exception e) {
                profileUpdateState.postValue(new ProfileUpdateState.Error(
                    e.getMessage() != null ? e.getMessage() : "An unexpected error occurred"
                ));
            }
        });
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
