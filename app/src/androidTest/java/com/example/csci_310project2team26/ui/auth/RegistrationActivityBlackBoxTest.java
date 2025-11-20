package com.example.csci_310project2team26.ui.auth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.util.TextInputLayoutMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for registration validation flows.
 */
@RunWith(AndroidJUnit4.class)
public class RegistrationActivityBlackBoxTest {

    @Rule
    public ActivityScenarioRule<RegistrationActivity> activityRule =
            new ActivityScenarioRule<>(RegistrationActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void registrationScreenDisplaysAllInputs() {
        onView(withId(R.id.nameEditText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.studentIdEditText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.confirmPasswordEditText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.registerButton)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void emptyNameShowsErrorOnRegister() {
        onView(withId(R.id.registerButton)).perform(scrollTo(), click());
        onView(withId(R.id.nameInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Name is required")));
    }

    @Test
    public void nonUscEmailShowsError() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("example@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.emailInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must be a USC email (@usc.edu)")));
    }

    @Test
    public void invalidEmailFormatShowsError() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("user@usc"), closeSoftKeyboard());
        onView(withId(R.id.emailInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Invalid email format")));
    }

    @Test
    public void emptyStudentIdShowsError() {
        onView(withId(R.id.registerButton)).perform(scrollTo(), click());
        onView(withId(R.id.studentIdInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Student ID is required")));
    }

    @Test
    public void shortStudentIdShowsError() {
        onView(withId(R.id.studentIdEditText)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.studentIdInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must be exactly 10 digits")));
    }

    @Test
    public void nonDigitStudentIdShowsError() {
        onView(withId(R.id.studentIdEditText)).perform(scrollTo(), replaceText("12345abcde"), closeSoftKeyboard());
        onView(withId(R.id.studentIdInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must contain only digits")));
    }

    @Test
    public void shortPasswordShowsError() {
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("short"), closeSoftKeyboard());
        onView(withId(R.id.passwordInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Password must be at least 8 characters")));
    }

    @Test
    public void mismatchedPasswordsShowError() {
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(scrollTo(), replaceText("different"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Passwords do not match")));
    }

    @Test
    public void helperTextsAreVisible() {
        onView(withText(R.string.must_end_with_usc_edu)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withText(R.string.ten_digit_id)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void validFieldsClearPreviousErrors() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("bad@gmail.com"));
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("short"));
        onView(withId(R.id.confirmPasswordEditText)).perform(scrollTo(), replaceText("shorter"));
        onView(withId(R.id.studentIdEditText)).perform(scrollTo(), replaceText("123"));

        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("student@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.studentIdEditText)).perform(scrollTo(), replaceText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());

        onView(withId(R.id.emailInputLayout)).check(matches(TextInputLayoutMatchers.hasNoTextInputLayoutError()));
        onView(withId(R.id.studentIdInputLayout)).check(matches(TextInputLayoutMatchers.hasNoTextInputLayoutError()));
        onView(withId(R.id.passwordInputLayout)).check(matches(TextInputLayoutMatchers.hasNoTextInputLayoutError()));
        onView(withId(R.id.confirmPasswordInputLayout)).check(matches(TextInputLayoutMatchers.hasNoTextInputLayoutError()));
    }

    @Test
    public void loginLinkNavigatesBackToLogin() {
        onView(withId(R.id.loginLink)).perform(scrollTo(), click());
        androidx.test.espresso.intent.Intents.intended(androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent(LoginActivity.class.getName()));
    }
}
