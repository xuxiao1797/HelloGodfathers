package com.example.hellogodfather;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.view.View;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
@LargeTest
public class SettingTest {
    @Rule
    public ActivityTestRule<Setting> mActivityTestRule = new ActivityTestRule<>(Setting.class);

    @Test
    public void testTextView(){
        onView(withId(R.id.tv_nickName)).check(matches(withText("nickname")));
        onView(withId(R.id.tv_userId)).check(matches(withText("userId")));
        onView(withId(R.id.tv_setting_settings)).check(matches(withText("Settings")));
        onView(withId(R.id.tv_setting_about)).check(matches(withText("About")));
        onView(withId(R.id.bt_setting_logout)).check(matches(withText("Log Out")));
    }

    @Test
    public void testButton(){
        onView(withId(R.id.bt_setting_logout)).check(matches(isClickable()));
        //onView(withId(R.id.tv_setting_settings)).perform(click()).check(matches(withText("BACK")));
        Matcher<View> viewMatcher = withId(R.id.tv_setting_settings);
        ViewInteraction viewInteraction = onView(viewMatcher);
        viewInteraction.perform(click());
        ViewAssertion viewAssertion = doesNotExist();
        ViewAssertions.matches(withText(""));
        viewInteraction.check(viewAssertion);

        onView(withText("save")).perform(click()).check(matches(isClickable()));

    }
}
