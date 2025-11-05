package com.example.csci_310project2team26.ui.create_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.csci_310project2team26.MainActivity;
import com.example.csci_310project2team26.databinding.ActivityProfileCreationBinding;
import com.example.csci_310project2team26.viewmodel.ProfileViewModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * ProfileCreationActivity - Handles initial profile setup after registration
 * Requirements: UR-2 Profile Creation & Management
 * 
 * Required fields:
 * - Name (pre-filled, non-editable)
 * - Email (pre-filled, non-editable)
 * - Affiliation (department/school)
 * - Birth date
 * - Bio
 * 
 * Optional fields:
 * - Profile picture
 * - Interests
 */
public class ProfileCreationActivity extends AppCompatActivity {
    
    private ActivityProfileCreationBinding binding;
    private ProfileViewModel profileViewModel;
    
    private Calendar selectedBirthDate = Calendar.getInstance();
    private Uri profileImageUri = null;
    private String userId = "";
    
    // USC Schools/Departments list
    private final String[] uscAffiliations = {
        "Viterbi School of Engineering",
        "Marshall School of Business",
        "Dornsife College of Letters, Arts and Sciences",
        "Annenberg School for Communication and Journalism",
        "School of Cinematic Arts",
        "Roski School of Art and Design",
        "School of Architecture",
        "Thornton School of Music",
        "Keck School of Medicine",
        "School of Pharmacy",
        "Suzanne Dworak-Peck School of Social Work",
        "Rossier School of Education",
        "Gould School of Law",
        "Price School of Public Policy",
        "School of Dramatic Arts",
        "Herman Ostrow School of Dentistry",
        "Other"
    };
    
    private final ActivityResultLauncher<String> imagePickerLauncher = 
        registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                profileImageUri = uri;
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.profileImageView);
            }
        });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        
        // Get user data from intent
        userId = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        
        if (userId == null) userId = "";
        if (userName == null) userName = "";
        if (userEmail == null) userEmail = "";
        
        setupUI(userName, userEmail);
        observeViewModel();
    }
    
    private void setupUI(String userName, String userEmail) {
        // Pre-fill non-editable fields
        binding.nameEditText.setText(userName);
        binding.nameEditText.setEnabled(false);
        
        binding.emailEditText.setText(userEmail);
        binding.emailEditText.setEnabled(false);
        
        // Setup affiliation dropdown
        ArrayAdapter<String> affiliationAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            uscAffiliations
        );
        binding.affiliationAutoComplete.setAdapter(affiliationAdapter);
        
        // Birth date picker
        binding.birthDateEditText.setOnClickListener(v -> showDatePicker());
        
        // Profile picture selection
        binding.changePhotoButton.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });
        
        // Create profile button
        binding.createProfileButton.setOnClickListener(v -> {
            if (validateFields()) {
                createProfile();
            }
        });
        
        // Skip for now (optional)
        binding.skipButton.setOnClickListener(v -> {
            Toast.makeText(this, "You can complete your profile later in settings", 
                Toast.LENGTH_LONG).show();
            navigateToMainApp();
        });
    }
    
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        // Set minimum age to 13 years (USC policy)
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, -13);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                selectedBirthDate.set(year, month, dayOfMonth);
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                binding.birthDateEditText.setText(dateFormat.format(selectedBirthDate.getTime()));
            },
            calendar.get(Calendar.YEAR) - 20,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set max date to 13 years ago
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        
        datePickerDialog.show();
    }
    
    private boolean validateFields() {
        boolean isValid = true;
        
        // Validate affiliation
        String affiliation = binding.affiliationAutoComplete.getText().toString();
        if (affiliation.isEmpty()) {
            binding.affiliationInputLayout.setError("Please select your school/department");
            isValid = false;
        } else {
            binding.affiliationInputLayout.setError(null);
        }
        
        // Validate birth date
        String birthDate = binding.birthDateEditText.getText().toString();
        if (birthDate.isEmpty()) {
            binding.birthDateInputLayout.setError("Birth date is required");
            isValid = false;
        } else {
            binding.birthDateInputLayout.setError(null);
        }
        
        // Validate bio
        String bio = binding.bioEditText.getText().toString();
        if (bio.isEmpty()) {
            binding.bioInputLayout.setError("Please add a short bio");
            isValid = false;
        } else if (bio.length() < 10) {
            binding.bioInputLayout.setError("Bio must be at least 10 characters");
            isValid = false;
        } else {
            binding.bioInputLayout.setError(null);
        }
        
        return isValid;
    }
    
    private void createProfile() {
        binding.createProfileButton.setEnabled(false);
        binding.progressBar.setVisibility(android.view.View.VISIBLE);
        
        String affiliation = binding.affiliationAutoComplete.getText().toString();
        String birthDate = binding.birthDateEditText.getText().toString();
        String bio = binding.bioEditText.getText().toString();
        String interests = binding.interestsEditText.getText().toString();
        
        profileViewModel.createProfile(
            userId,
            affiliation,
            birthDate,
            bio,
            interests.isEmpty() ? null : interests,
            profileImageUri
        );
    }
    
    private void navigateToMainApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void observeViewModel() {
        profileViewModel.getProfileCreationState().observe(this, state -> {
            binding.progressBar.setVisibility(android.view.View.GONE);
            binding.createProfileButton.setEnabled(true);
            
            if (state instanceof ProfileViewModel.ProfileCreationState.Success) {
                Toast.makeText(this, "Profile created successfully!", 
                    Toast.LENGTH_SHORT).show();
                navigateToMainApp();
            } else if (state instanceof ProfileViewModel.ProfileCreationState.Error) {
                ProfileViewModel.ProfileCreationState.Error errorState = 
                    (ProfileViewModel.ProfileCreationState.Error) state;
                Toast.makeText(this, "Failed to create profile: " + errorState.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
}
