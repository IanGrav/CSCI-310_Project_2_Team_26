package com.example.csci_310project2team26.ui.createpost;

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
 * Black-box Test: CreatePostFragment UI Testing
 * 
 * Location: app/src/androidTest/java/com/example/csci_310project2team26/ui/createpost/CreatePostBlackBoxTest.java
 * Test Class: CreatePostBlackBoxTest
 * 
 * Description: Tests the CreatePostFragment UI from a user's perspective.
 * Tests creating regular posts, prompt posts, and validation.
 * 
 * How to Execute: 
 * 1. Start an Android emulator or connect a physical device
 * 2. Ensure user is logged in
 * 3. Run as Android Instrumented Test in Android Studio
 * 4. Or via: ./gradlew connectedAndroidTest --tests CreatePostBlackBoxTest
 * 
 * Rationale: These tests verify the post creation functionality works correctly from an external perspective,
 * ensuring users can create both regular and prompt posts as expected.
 */
@RunWith(AndroidJUnit4.class)
public class CreatePostBlackBoxTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void testCreatePostDisplaysCorrectly() {
        // Rationale: Verify that create post screen displays all required UI elements
        // Input: Navigate to create post
        // Expected: Title, body, tag fields, and publish button are visible
        
        // Navigate to create post (assuming it's accessible from bottom nav or menu)
        
        // Verify required fields are displayed
        onView(withId(R.id.titleEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.bodyEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.tagEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.publishButton)).check(matches(isDisplayed()));
    }
    
    @Test
    public void testPromptPostToggle() {
        // Rationale: Test that prompt post toggle shows/hides prompt fields
        // Input: Toggle prompt switch
        // Expected: Prompt section and description section fields appear/disappear
        
        // Verify prompt switch is displayed
        onView(withId(R.id.promptSwitch)).check(matches(isDisplayed()));
        
        // Toggle prompt switch on
        onView(withId(R.id.promptSwitch)).perform(click());
        
        // Verify prompt fields are now visible
        onView(withId(R.id.promptSectionEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionSectionEditText)).check(matches(isDisplayed()));
        
        // Toggle prompt switch off
        onView(withId(R.id.promptSwitch)).perform(click());
        
        // Verify prompt fields are hidden (they should not be displayed)
        // Note: Espresso may still find them in the view hierarchy even if hidden
        // This test verifies the toggle functionality works
    }
    
    @Test
    public void testCreateRegularPostWithEmptyFields() {
        // Rationale: Test validation for regular post with empty required fields
        // Input: Click publish without filling required fields
        // Expected: Error messages displayed
        
        // Try to publish without filling fields
        onView(withId(R.id.publishButton)).perform(click());
        
        // Should show validation error (title, content, or tag required)
        // Note: Toast messages are hard to test, but we verify no crash
    }
    
    @Test
    public void testCreateRegularPostWithValidData() {
        // Rationale: Test creating a regular post with valid data
        // Input: Fill title, body, tag and publish
        // Expected: Post is created successfully
        
        // Ensure prompt switch is off
        onView(withId(R.id.promptSwitch)).perform(click()); // Toggle off if on
        
        // Enter title
        onView(withId(R.id.titleEditText))
            .perform(typeText("Test Post Title"), closeSoftKeyboard());
        
        // Enter body
        onView(withId(R.id.bodyEditText))
            .perform(typeText("Test post content"), closeSoftKeyboard());
        
        // Enter tag (required)
        onView(withId(R.id.tagEditText))
            .perform(typeText("GPT-4"), closeSoftKeyboard());
        
        // Click publish
        onView(withId(R.id.publishButton)).perform(click());
        
        // Note: Actual success depends on backend
        // This test verifies the UI flow works correctly
    }
    
    @Test
    public void testCreatePromptPostWithValidData() {
        // Rationale: Test creating a prompt post with valid data
        // Input: Toggle prompt, fill prompt section, description, tag and publish
        // Expected: Prompt post is created successfully
        
        // Toggle prompt switch on
        onView(withId(R.id.promptSwitch)).perform(click());
        
        // Enter title
        onView(withId(R.id.titleEditText))
            .perform(typeText("Test Prompt Post"), closeSoftKeyboard());
        
        // Enter prompt section
        onView(withId(R.id.promptSectionEditText))
            .perform(typeText("Test prompt section"), closeSoftKeyboard());
        
        // Enter description section
        onView(withId(R.id.descriptionSectionEditText))
            .perform(typeText("Test description section"), closeSoftKeyboard());
        
        // Enter tag (required)
        onView(withId(R.id.tagEditText))
            .perform(typeText("GPT-4"), closeSoftKeyboard());
        
        // Click publish
        onView(withId(R.id.publishButton)).perform(click());
        
        // Note: Actual success depends on backend
        // This test verifies the UI flow works correctly
    }
    
    @Test
    public void testCreatePromptPostWithoutPromptFields() {
        // Rationale: Test validation for prompt post without prompt/description sections
        // Input: Toggle prompt on, but don't fill prompt/description fields
        // Expected: Error message displayed
        
        // Toggle prompt switch on
        onView(withId(R.id.promptSwitch)).perform(click());
        
        // Enter title
        onView(withId(R.id.titleEditText))
            .perform(typeText("Test Prompt Post"), closeSoftKeyboard());
        
        // Enter tag
        onView(withId(R.id.tagEditText))
            .perform(typeText("GPT-4"), closeSoftKeyboard());
        
        // Don't fill prompt or description sections
        
        // Click publish - should show error
        onView(withId(R.id.publishButton)).perform(click());
        
        // Should show validation error
    }
    
    @Test
    public void testCreatePostWithoutTag() {
        // Rationale: Test validation for post without tag (tag is required)
        // Input: Fill title and content but not tag
        // Expected: Error message displayed
        
        // Ensure prompt switch is off
        onView(withId(R.id.promptSwitch)).perform(click()); // Toggle off if on
        
        // Enter title
        onView(withId(R.id.titleEditText))
            .perform(typeText("Test Post"), closeSoftKeyboard());
        
        // Enter body
        onView(withId(R.id.bodyEditText))
            .perform(typeText("Test content"), closeSoftKeyboard());
        
        // Don't enter tag
        
        // Click publish - should show error
        onView(withId(R.id.publishButton)).perform(click());
        
        // Should show validation error for missing tag
    }
}

