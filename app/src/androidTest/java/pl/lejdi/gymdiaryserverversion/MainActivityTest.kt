package pl.lejdi.gymdiaryserverversion

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.lejdi.gymdiaryserverversion.ui.MainActivity


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest
{
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun is_everything_visible()
    {
        onView(withId(R.id.main_activity_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.container)).check(matches(isDisplayed()))
    }
}