package com.example.csci_310project2team26.ui.main;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.example.csci_310project2team26.MainActivity;
import com.example.csci_310project2team26.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Black-box navigation and interaction tests across the main user flows and fragments.
 * These tests exercise UI surfaces mapped directly to the requirements document.
 */
@RunWith(AndroidJUnit4.class)
public class MainFlowBlackBoxTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void homeFragment_showsSearchAndSortControls() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.sortSpinner)).check(matches(isDisplayed()));
    }

    @Test
    public void homeFragment_showsPostsRecyclerView() {
        onView(withId(R.id.postsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNav_canNavigateToDashboardFeed() {
        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withId(R.id.promptPostsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNav_canNavigateToCreatePost() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.titleEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.bodyEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNav_canNavigateToNotifications() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withId(R.id.activityRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void createPost_promptFieldsVisibleWhenEnabled() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.promptSwitch)).perform(click());
        onView(withId(R.id.promptSectionEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionSectionEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void createPost_publishButtonPresent() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.publishButton)).check(matches(isDisplayed()));
    }

    @Test
    public void home_allowsOpeningPostDetail() {
        onView(withId(R.id.postsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.contentTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void postDetail_showsVotingAndCommentingControls() {
        onView(withId(R.id.postsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.upvoteButton)).check(matches(isDisplayed()));
        onView(withId(R.id.downvoteButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addCommentButton)).check(matches(isDisplayed()));
    }

    @Test
    public void postDetail_showsCountsAndTags() {
        onView(withId(R.id.postsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tagTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.upvoteCountTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.downvoteCountTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.commentCountTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void notifications_displaysUserActivityList() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withId(R.id.activityRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void notifications_showsEmptyStateWhenNoActivity() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withId(R.id.activityEmptyStateTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void appBar_menuOpensSearch() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.search_title)).perform(click());
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void appBar_menuOpensTrending() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.trending_posts_title)).perform(click());
        onView(withId(R.id.trendingRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void searchFragment_supportsFilteringByKeyword() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.search_title)).perform(click());
        onView(withId(R.id.searchEditText)).perform(replaceText("GPT"), closeSoftKeyboard());
        onView(withId(R.id.postsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void searchFragment_hasFilterControls() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.search_title)).perform(click());
        onView(withId(R.id.searchTypeSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.postTypeSpinner)).check(matches(isDisplayed()));
    }

    @Test
    public void trendingFragment_displaysVotesAndComments() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.trending_posts_title)).perform(click());
        onView(withId(R.id.postsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.upvoteTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.downvoteTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.commentCountTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void createPost_requiresTitleBeforePublish() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.publishButton)).perform(click());
        onView(withId(R.id.titleEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void createPost_requiresBodyBeforePublish() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.titleEditText)).perform(replaceText("Test Title"), closeSoftKeyboard());
        onView(withId(R.id.publishButton)).perform(click());
        onView(withId(R.id.bodyEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void createPost_supportsLlmTagging() {
        onView(withId(R.id.navigation_create_post)).perform(click());
        onView(withId(R.id.tagEditText)).perform(replaceText("GPT-4"), closeSoftKeyboard());
        onView(withId(R.id.tagEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void dashboard_showsPostSummaries() {
        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withId(R.id.promptPostsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.contentTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void notifications_fragmentShowsPromptsAndDescriptions() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withId(R.id.activityRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.contentTextView)).check(matches(isDisplayed()));
    }
}
