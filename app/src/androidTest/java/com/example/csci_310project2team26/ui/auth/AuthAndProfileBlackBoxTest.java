package com.example.csci_310project2team26.ui.auth;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.ui.create_profile.ProfileCreationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Black-box tests that validate authentication, registration, and profile creation screens
 * against the high-level user requirements. These tests avoid implementation knowledge and
 * verify that the required inputs and flows are exposed to end users.
 */
@RunWith(AndroidJUnit4.class)
public class AuthAndProfileBlackBoxTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void loginScreen_hasRequiredFields() {
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.registerLink)).check(matches(isDisplayed()));
    }

    @Test
    public void login_rejectsNonUscEmail() {
        onView(withId(R.id.emailEditText)).perform(typeText("student@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Must be a USC email (@usc.edu)")).check(matches(isDisplayed()));
    }

    @Test
    public void login_showsErrorWhenEmailEmpty() {
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Email is required")).check(matches(isDisplayed()));
    }

    @Test
    public void login_showsErrorWhenPasswordEmpty() {
        onView(withId(R.id.emailEditText)).perform(typeText("test@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Password is required")).check(matches(isDisplayed()));
    }

    @Test
    public void login_rememberMeCanBeChecked() {
        onView(withId(R.id.rememberMeCheckbox)).perform(click());
        onView(withId(R.id.rememberMeCheckbox)).check(matches(isChecked()));
    }

    @Test
    public void login_forgotPasswordLinkVisible() {
        onView(withId(R.id.forgotPasswordLink)).check(matches(isDisplayed()));
    }

    @Test
    public void login_registerLinkNavigatesToRegistration() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.studentIdEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void registrationScreen_hasAllInputs() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.studentIdEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.confirmPasswordEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void registration_enforcesUscEmailFormat() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText("person@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Must be a USC email (@usc.edu)")).check(matches(isDisplayed()));
    }

    @Test
    public void registration_requiresTenDigitStudentId() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.studentIdEditText)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Student ID must be a 10-digit number"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void registration_requiresStrongPasswordAndConfirmation() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.passwordEditText)).perform(typeText("short"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("different"), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText("Password must be at least 8 characters"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void registration_hasLinkBackToLogin() {
        onView(withId(R.id.registerLink)).perform(click());
        onView(withId(R.id.loginLink)).check(matches(isDisplayed()));
    }

    @Test
    public void profileCreation_hasAllRequiredFields() {
        ActivityScenario<ProfileCreationActivity> scenario =
                ActivityScenario.launch(ProfileCreationActivity.class);
        onView(withId(R.id.profileImageView)).check(matches(isDisplayed()));
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.affiliationAutoComplete)).check(matches(isDisplayed()));
        onView(withId(R.id.birthDateEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.bioEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.interestsEditText)).check(matches(isDisplayed()));
        scenario.close();
    }

    @Test
    public void profileCreation_allowsSkipProfileSetup() {
        ActivityScenario<ProfileCreationActivity> scenario =
                ActivityScenario.launch(ProfileCreationActivity.class);
        onView(withId(R.id.skipButton)).check(matches(isDisplayed()));
        scenario.close();
    }

    @Test
    public void profileCreation_changePhotoButtonVisible() {
        ActivityScenario<ProfileCreationActivity> scenario =
                ActivityScenario.launch(ProfileCreationActivity.class);
        onView(withId(R.id.changePhotoButton)).check(matches(isDisplayed()));
        scenario.close();
    }
}
