package com.example.csci_310project2team26.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csci_310project2team26.data.model.User;
import com.example.csci_310project2team26.data.repository.AuthRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AuthViewModel - Handles authentication business logic
 * Implements MVVM pattern as per architectural design
 */
public class AuthViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    private final ExecutorService executorService;
    
    // Registration State
    private final MutableLiveData<RegistrationState> registrationState = new MutableLiveData<>();
    
    // Login State
    private final MutableLiveData<LoginState> loginState = new MutableLiveData<>();
    
    public AuthViewModel() {
        this.authRepository = new AuthRepository();
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    // State classes
    public static abstract class RegistrationState {
        public static class Idle extends RegistrationState {}
        public static class Loading extends RegistrationState {}
        public static class Success extends RegistrationState {
            private final String userId;
            public Success(String userId) {
                this.userId = userId;
            }
            public String getUserId() {
                return userId;
            }
        }
        public static class Error extends RegistrationState {
            private final String message;
            public Error(String message) {
                this.message = message;
            }
            public String getMessage() {
                return message;
            }
        }
    }
    
    public static abstract class LoginState {
        public static class Idle extends LoginState {}
        public static class Loading extends LoginState {}
        public static class Success extends LoginState {
            private final User user;
            public Success(User user) {
                this.user = user;
            }
            public User getUser() {
                return user;
            }
        }
        public static class Error extends LoginState {
            private final String message;
            public Error(String message) {
                this.message = message;
            }
            public String getMessage() {
                return message;
            }
        }
        public static class SessionExists extends LoginState {
            private final User user;
            public SessionExists(User user) {
                this.user = user;
            }
            public User getUser() {
                return user;
            }
        }
    }
    
    public LiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }
    
    public LiveData<LoginState> getLoginState() {
        return loginState;
    }
    
    /**
     * Register a new USC user
     * Validates USC email domain and 10-digit student ID
     */
    public void register(String name, String email, String studentId, String password) {
        registrationState.postValue(new RegistrationState.Loading());
        
        executorService.execute(() -> {
            try {
                // Validate USC email
                if (!email.endsWith("@usc.edu")) {
                    registrationState.postValue(new RegistrationState.Error("Must use USC email"));
                    return;
                }
                
                // Validate student ID length
                if (studentId.length() != 10) {
                    registrationState.postValue(new RegistrationState.Error("Student ID must be 10 digits"));
                    return;
                }
                
                // Call repository to register user
                authRepository.register(name, email, studentId, password, new AuthRepository.Callback<String>() {
                    @Override
                    public void onSuccess(String userId) {
                        registrationState.postValue(new RegistrationState.Success(userId));
                    }
                    
                    @Override
                    public void onError(String error) {
                        registrationState.postValue(new RegistrationState.Error(error));
                    }
                });
                
            } catch (Exception e) {
                registrationState.postValue(new RegistrationState.Error(
                    e.getMessage() != null ? e.getMessage() : "An unexpected error occurred"
                ));
            }
        });
    }
    
    /**
     * Login with USC email and password
     */
    public void login(String email, String password, boolean rememberMe) {
        loginState.postValue(new LoginState.Loading());
        
        executorService.execute(() -> {
            try {
                // Validate USC email
                if (!email.endsWith("@usc.edu")) {
                    loginState.postValue(new LoginState.Error("Must use USC email"));
                    return;
                }
                
                // Call repository to authenticate user
                authRepository.login(email, password, rememberMe, new AuthRepository.Callback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        loginState.postValue(new LoginState.Success(user));
                    }
                    
                    @Override
                    public void onError(String error) {
                        loginState.postValue(new LoginState.Error(error));
                    }
                });
                
            } catch (Exception e) {
                loginState.postValue(new LoginState.Error(
                    e.getMessage() != null ? e.getMessage() : "An unexpected error occurred"
                ));
            }
        });
    }
    
    /**
     * Check if user has a saved session
     */
    public void checkSavedSession() {
        executorService.execute(() -> {
            try {
                authRepository.checkSavedSession(new AuthRepository.Callback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            loginState.postValue(new LoginState.SessionExists(user));
                        }
                    }
                    
                    @Override
                    public void onError(String error) {
                        // No saved session or error - do nothing
                    }
                });
            } catch (Exception e) {
                // No saved session or error - do nothing
            }
        });
    }
    
    /**
     * Logout user
     */
    public void logout() {
        executorService.execute(() -> {
            authRepository.logout();
        });
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
