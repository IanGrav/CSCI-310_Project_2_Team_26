package com.example.csci_310project2team26.ui.auth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.util.TextInputLayoutMatchers;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box UI coverage for LoginActivity validation and navigation affordances.
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
    public void loginScreenRendersKeyElements() {
        onView(withId(R.id.logoImageView)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.loginButton)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.registerLink)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void nonUscEmailShowsError() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("test@gmail.com"));
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());

        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must be a USC email (@usc.edu)")));
    }

    @Test
    public void correctingEmailClearsError() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("test@gmail.com"));
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());
        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must be a USC email (@usc.edu)")));

        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("valid@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasNoTextInputLayoutError()));
    }

    @Test
    public void emptyEmailShowsValidationErrorOnSubmit() {
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(scrollTo(), click());

        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Email is required")));
        onView(withId(R.id.loginButton)).check(ViewAssertions.matches(isEnabled()));
    }

    @Test
    public void emptyPasswordShowsValidationErrorOnSubmit() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("user@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(scrollTo(), click());

        onView(withId(R.id.passwordInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Password is required")));
    }

    @Test
    public void emptyFieldsShowBothErrors() {
        onView(withId(R.id.loginButton)).perform(scrollTo(), click());

        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Email is required")));
        onView(withId(R.id.passwordInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Password is required")));
    }

    @Test
    public void rememberMeCheckboxToggles() {
        onView(withId(R.id.rememberMeCheckbox)).perform(scrollTo(), click());
        onView(withId(R.id.rememberMeCheckbox)).check(ViewAssertions.matches(isChecked()));
    }

    @Test
    public void forgotPasswordLinkShowsToast() {
        final View[] decorView = new View[1];
        activityRule.getScenario().onActivity(activity -> decorView[0] = activity.getWindow().getDecorView());

        onView(withId(R.id.forgotPasswordLink)).perform(scrollTo(), click());

        onView(withText("Forgot Password feature coming soon"))
                .inRoot(org.hamcrest.Matchers.not(ViewMatchers.withDecorView(org.hamcrest.Matchers.is(decorView[0]))))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void registerLinkNavigatesToRegistration() {
        onView(withId(R.id.registerLink)).perform(scrollTo(), click());
        androidx.test.espresso.intent.Intents.intended(androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent(RegistrationActivity.class.getName()));
    }

    @Test
    public void loginRejectsNonUscEmailOnSubmit() {
        onView(withId(R.id.emailEditText)).perform(scrollTo(), replaceText("user@gmail.com"));
        onView(withId(R.id.passwordEditText)).perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(scrollTo(), click());

        onView(withId(R.id.emailInputLayout))
                .check(ViewAssertions.matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Must be a USC email (@usc.edu)")));
    }

    @Test
    public void passwordToggleIconIsVisible() {
        onView(withId(R.id.passwordInputLayout))
                .check(ViewAssertions.matches(hasEndIcon()));
    }

    private static Matcher<View> hasEndIcon() {
        return ViewMatchers.hasDescendant(withId(com.google.android.material.R.id.text_input_end_icon));
    }
}
