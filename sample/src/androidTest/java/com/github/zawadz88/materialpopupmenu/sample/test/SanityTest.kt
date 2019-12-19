package com.github.zawadz88.materialpopupmenu.sample.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.zawadz88.materialpopupmenu.sample.SampleActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.github.zawadz88.materialpopupmenu.sample.R as sampleR

@RunWith(AndroidJUnit4::class)
class SanityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<SampleActivity>(SampleActivity::class.java)

    @Test
    fun should_open_popup_menu() {
        // when
        onView(withId(sampleR.id.showPopupButton)).perform(click())

        // then
        onView(withText("Copy")).inRoot(isPlatformPopup()).check(matches(isDisplayed()))
    }
}
