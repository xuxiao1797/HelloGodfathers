package com.example.hellogodfather;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
@LargeTest
public class LoginTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testTextView(){
        onView(withId(R.id.login_title)).check(matches(withText("God Fathers")));
        onView(withId(R.id.login_button)).check(matches(withText("Become Godfathers!")));
    }


    public void testButton(){

    }
}
