package com.example.csci_310project2team26.ui.auth;

import android.view.View;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.csci_310project2team26.R;

import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.equalTo;

/**
 * Black-box Test: LoginActivity UI Testing
 * 
 * Location: app/src/androidTest/java/com/example/csci_310project2team26/ui/auth/LoginActivityBlackBoxTest.java
 * Test Class: LoginActivityBlackBoxTest
 * 
 * Description: Tests the LoginActivity UI from a user's perspective without knowledge of internal implementation.
 * Tests user interactions, input validation, and navigation flows.
 * 
 * How to Execute: 
 * 1. Start an Android emulator or connect a physical device
 * 2. Run as Android Instrumented Test in Android Studio
 * 3. Or via: ./gradlew connectedAndroidTest --tests LoginActivityBlackBoxTest
 * 
 * Rationale: These tests verify the user interface behaves correctly from an external perspective,
 * ensuring users can interact with login functionality as expected.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityBlackBoxTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
        new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }
    
    @Test

    public void testLoginActivityDisplaysCorrectly() {
        onView(withId(R.id.logoImageView)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.rememberMeCheckbox)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.registerLink)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithMissingEmailShowsError() {
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.emailInputLayout))
                .check(matches(textInputLayoutErrorIs("Email is required")));
        onView(withId(R.id.passwordInputLayout))
                .check(matches(textInputLayoutErrorIs("Password is required")));
    }

    @Test
    public void testLoginWithNonUscEmailShowsValidationMessage() {
        onView(withId(R.id.emailEditText))
                .perform(typeText("student@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(typeText("password123"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.emailInputLayout))
                .check(matches(textInputLayoutErrorIs("Must be a USC email (@usc.edu)")));
    }

    @Test
    public void testLoginWithMissingPasswordShowsError() {
        onView(withId(R.id.emailEditText))
                .perform(typeText("valid@usc.edu"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.passwordInputLayout))
                .check(matches(textInputLayoutErrorIs("Password is required")));
    }

    @Test
    public void testNavigateToRegistration() {
        onView(withId(R.id.registerLink)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(RegistrationActivity.class.getName()));
    }

    private static org.hamcrest.Matcher<View> textInputLayoutErrorIs(String expectedError) {
        return new org.hamcrest.TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof com.google.android.material.textfield.TextInputLayout)) {
                    return false;
                }
                com.google.android.material.textfield.TextInputLayout layout =
                        (com.google.android.material.textfield.TextInputLayout) item;
                CharSequence error = layout.getError();
                return expectedError.equals(error);
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with TextInputLayout error: ")
                        .appendValue(expectedError);
            }
        };
    }
}

