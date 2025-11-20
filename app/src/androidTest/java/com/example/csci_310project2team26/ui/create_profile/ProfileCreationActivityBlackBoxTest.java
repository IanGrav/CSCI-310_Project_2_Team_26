package com.example.csci_310project2team26.ui.create_profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.util.TextInputLayoutMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box validation coverage for ProfileCreationActivity.
 */
@RunWith(AndroidJUnit4.class)
public class ProfileCreationActivityBlackBoxTest {

    private static Intent launchIntent() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ProfileCreationActivity.class);
        intent.putExtra("USER_ID", "123");
        intent.putExtra("USER_NAME", "Test User");
        intent.putExtra("USER_EMAIL", "user@usc.edu");
        return intent;
    }

    @Rule
    public ActivityScenarioRule<ProfileCreationActivity> activityRule =
            new ActivityScenarioRule<>(launchIntent());

    @Test
    public void prefilledNameAndEmailAreDisabled() {
        onView(withId(R.id.nameEditText)).perform(scrollTo()).check(matches(withText("Test User")));
        onView(withId(R.id.nameEditText)).check(matches(not(isEnabled())));
        onView(withId(R.id.emailEditText)).perform(scrollTo()).check(matches(withText("user@usc.edu")));
        onView(withId(R.id.emailEditText)).check(matches(not(isEnabled())));
    }

    @Test
    public void createWithoutRequiredFieldsShowsErrors() {
        onView(withId(R.id.createProfileButton)).perform(scrollTo(), click());

        onView(withId(R.id.affiliationInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Please select your school/department")));
        onView(withId(R.id.birthDateInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Birth date is required")));
        onView(withId(R.id.bioInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Please add a short bio")));
    }

    @Test
    public void shortBioShowsValidationError() {
        onView(withId(R.id.affiliationAutoComplete)).perform(scrollTo(), replaceText("Viterbi School of Engineering"));
        onView(withId(R.id.birthDateEditText)).perform(scrollTo(), replaceText("01/01/2000"));
        onView(withId(R.id.bioEditText)).perform(scrollTo(), replaceText("Too short"));

        onView(withId(R.id.createProfileButton)).perform(scrollTo(), click());

        onView(withId(R.id.bioInputLayout))
                .check(matches(TextInputLayoutMatchers.hasTextInputLayoutErrorText("Bio must be at least 10 characters")));
    }

    @Test
    public void affiliationFieldAcceptsDropdownText() {
        onView(withId(R.id.affiliationAutoComplete)).perform(scrollTo(), replaceText("Marshall School of Business"));
        onView(withId(R.id.affiliationAutoComplete)).check(matches(withText("Marshall School of Business")));
    }

    @Test
    public void skipButtonShowsGuidanceToast() {
        final View[] decorView = new View[1];
        activityRule.getScenario().onActivity(activity -> decorView[0] = activity.getWindow().getDecorView());

        onView(withId(R.id.skipButton)).perform(scrollTo(), click());

        onView(withText("You can complete your profile later in settings"))
                .inRoot(org.hamcrest.Matchers.not(ViewMatchers.withDecorView(org.hamcrest.Matchers.is(decorView[0]))))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
