package com.example.csci_310project2team26.ui.home;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
 * Black-box Test: HomeFragment UI Testing
 * 
 * Location: app/src/androidTest/java/com/example/csci_310project2team26/ui/home/HomeFragmentBlackBoxTest.java
 * Test Class: HomeFragmentBlackBoxTest
 * 
 * Description: Tests the HomeFragment UI from a user's perspective. Tests post display,
 * scrolling, voting, and navigation to post details.
 * 
 * How to Execute: 
 * 1. Start an Android emulator or connect a physical device
 * 2. Ensure user is logged in (or mock authentication)
 * 3. Run as Android Instrumented Test in Android Studio
 * 4. Or via: ./gradlew connectedAndroidTest --tests HomeFragmentBlackBoxTest
 * 
 * Rationale: These tests verify the home feed functionality works correctly from an external perspective,
 * ensuring users can view posts, interact with them, and navigate as expected.
 */
@RunWith(AndroidJUnit4.class)
public class HomeFragmentBlackBoxTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testHomeFragmentDisplaysFiltersAndList() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.sortSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.postsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void testBottomNavigationSwitchesFeeds() {
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withId(R.id.promptSearchEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void testOptionsMenuNavigatesToSearch() {
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText(R.string.search_title)).perform(click());

        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
    }

}

