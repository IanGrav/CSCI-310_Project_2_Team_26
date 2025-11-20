package com.example.csci_310project2team26.api;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci_310project2team26.MainActivity;
import com.example.csci_310project2team26.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Black-box tests that exercise the top-level navigation items which bridge the app's
 * feed, search, trending, and profile experiences. These tests rely solely on Espresso
 * interactions to verify that the correct screens are presented from the app bar menu
 * without depending on internal implementation details or backend availability.
 */
@RunWith(AndroidJUnit4.class)
public class APIIntegrationBlackBoxTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void openingSearchFromMenuShowsSearchControls() {
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText(R.string.search_title)).perform(click());

        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.searchTypeSpinner)).check(matches(isDisplayed()));
    }

    @Test
    public void openingTrendingFromMenuShowsTrendingFeed() {
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText(R.string.trending_posts_title)).perform(click());

        onView(withText(R.string.trending_posts_title)).check(matches(isDisplayed()));
        onView(withId(R.id.postsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void openingProfileSettingsFromMenuShowsProfileFields() {
        onView(withId(R.id.action_profile_settings)).perform(click());

        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
    }
}
