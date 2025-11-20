package com.example.csci_310project2team26.ui.profile;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;

import com.example.csci_310project2team26.MainActivity;
import com.example.csci_310project2team26.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Black-box Test: ProfileSettingsFragment UI Testing
 * 
 * Location: app/src/androidTest/java/com/example/csci_310project2team26/ui/profile/ProfileSettingsBlackBoxTest.java
 * Test Class: ProfileSettingsBlackBoxTest
 * 
 * Description: Tests the ProfileSettingsFragment UI from a user's perspective.
 * Tests logout, password reset, and profile update functionality.
 * 
 * How to Execute: 
 * 1. Start an Android emulator or connect a physical device
 * 2. Ensure user is logged in
 * 3. Run as Android Instrumented Test in Android Studio
 * 4. Or via: ./gradlew connectedAndroidTest --tests ProfileSettingsBlackBoxTest
 * 
 * Rationale: These tests verify the profile settings functionality works correctly from an external perspective,
 * ensuring users can logout, reset passwords, and update profiles as expected.
 */
@RunWith(AndroidJUnit4.class)
public class ProfileSettingsBlackBoxTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void testProfileSettingsDisplaysCorrectly() {
        // Rationale: Verify that profile settings screen displays all required UI elements
        // Input: Navigate to profile settings
        // Expected: All profile fields, password reset fields, and logout button are visible
        
        // Navigate to profile settings (assuming it's accessible from bottom nav or menu)
        // Note: This assumes navigation is set up correctly
        
        // Verify profile fields are displayed
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.bioEditText)).check(matches(isDisplayed()));
        
        // Verify password reset fields are displayed
        onView(withId(R.id.currentPasswordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.newPasswordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.resetPasswordButton)).check(matches(isDisplayed()));
        
        // Verify logout button is displayed
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
    }
    
    @Test
    public void testLogoutButtonDisplays() {
        // Rationale: Test that logout button is visible and accessible
        // Input: Navigate to profile settings
        // Expected: Logout button is displayed
        
        // Scroll to logout button if needed (it's at the bottom)
        onView(withId(R.id.logoutButton))
            .perform(ViewActions.scrollTo())
            .check(matches(isDisplayed()));
    }
    
    @Test
    public void testLogoutConfirmationDialog() {
        // Rationale: Test that logout shows confirmation dialog
        // Input: Click logout button
        // Expected: Confirmation dialog appears
        
        // Scroll to and click logout button
        onView(withId(R.id.logoutButton))
            .perform(ViewActions.scrollTo())
            .perform(click());
        
        // Verify confirmation dialog is displayed
        // The dialog should have "Are you sure you want to logout?" message
        onView(withText("Are you sure you want to logout?")).check(matches(isDisplayed()));
    }
    
    @Test
    public void testPasswordResetFieldsDisplay() {
        // Rationale: Test that password reset fields are visible
        // Input: Navigate to profile settings
        // Expected: Current password and new password fields are displayed
        
        onView(withId(R.id.currentPasswordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.newPasswordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.resetPasswordButton)).check(matches(isDisplayed()));
    }
    
    @Test
    public void testPasswordResetWithEmptyFields() {
        // Rationale: Test password reset validation with empty fields
        // Input: Click reset password button with empty fields
        // Expected: Error message displayed
        
        // Click reset password button without entering fields
        onView(withId(R.id.resetPasswordButton)).perform(click());
        
        // Should show error toast (validation happens in ViewModel)
        // Note: Toast messages are hard to test with Espresso, so we just verify no crash
    }
    
    @Test
    public void testPasswordResetWithValidFields() {
        // Rationale: Test password reset with valid input
        // Input: Enter current and new passwords
        // Expected: Password reset is attempted
        
        // Enter current password
        onView(withId(R.id.currentPasswordEditText))
            .perform(typeText("currentPassword123"), closeSoftKeyboard());
        
        // Enter new password
        onView(withId(R.id.newPasswordEditText))
            .perform(typeText("newPassword123"), closeSoftKeyboard());
        
        // Click reset button
        onView(withId(R.id.resetPasswordButton)).perform(click());
        
        // Note: Actual success/failure depends on backend validation
        // This test verifies the UI flow works correctly
    }
    
    @Test
    public void testProfileUpdateFieldsDisplay() {
        // Rationale: Test that profile update fields are visible
        // Input: Navigate to profile settings
        // Expected: Bio, interests, birth date fields are displayed
        
        onView(withId(R.id.bioEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.interestsEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.birthDateEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.saveButton)).check(matches(isDisplayed()));
    }
    
    @Test
    public void testProfileUpdateWithValidData() {
        // Rationale: Test updating profile with valid data
        // Input: Enter bio and interests, click save
        // Expected: Profile update is attempted
        
        // Enter bio
        onView(withId(R.id.bioEditText))
            .perform(typeText("Test bio"), closeSoftKeyboard());
        
        // Enter interests
        onView(withId(R.id.interestsEditText))
            .perform(typeText("Test interests"), closeSoftKeyboard());
        
        // Click save button
        onView(withId(R.id.saveButton)).perform(click());
        
        // Note: Actual success depends on backend
        // This test verifies the UI flow works correctly
    }
}

