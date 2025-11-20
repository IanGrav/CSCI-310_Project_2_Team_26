package com.example.csci_310project2team26.util;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Espresso matchers for asserting TextInputLayout error states in black-box UI tests.
 */
public final class TextInputLayoutMatchers {

    private TextInputLayoutMatchers() {}

    /**
     * Matches when a TextInputLayout displays the expected error text.
     */
    public static Matcher<View> hasTextInputLayoutErrorText(String expectedErrorText) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }
                CharSequence error = ((TextInputLayout) view).getError();
                return error != null && error.toString().contentEquals(expectedErrorText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with TextInputLayout error: ")
                        .appendValue(expectedErrorText);
            }
        };
    }

    /**
     * Matches when a TextInputLayout has no error message.
     */
    public static Matcher<View> hasNoTextInputLayoutError() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }
                return ((TextInputLayout) view).getError() == null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with no TextInputLayout error");
            }
        };
    }
}
