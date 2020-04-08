package pl.lejdi.gymdiary

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import pl.lejdi.gymdiary.ui.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Test
    fun test_navigation_add_new_exercise()
    {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        //training list visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))

        //go to add new exercise
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Add exercise")).perform(click()).also { Thread.sleep(500) }
        onView(withId(R.id.exercise_details_layout)).check(matches(isDisplayed()))

        //fill exercise with data
        onView(withId(R.id.exercise_details_name_notexists)).perform(typeText("espresso test"))
        closeSoftKeyboard()
        onView(withId(R.id.exercise_details_description)).perform(typeText("espresso test"))
        closeSoftKeyboard()
        onView(withId(R.id.RM_edittext)).perform(typeText("0"))
        closeSoftKeyboard()

        //save exercise
        onView(withId(R.id.exercise_details_fab)).perform(click())
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigation_add_new_training()
    {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_training)).perform(click()).also { Thread.sleep(500) }

        //check is add training is visible
        onView(withId(R.id.new_training_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.new_training_description)).perform(typeText("espresso test"))
        closeSoftKeyboard()
        onView(withId(R.id.new_training_fab)).perform(click())

        //training list should be visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigation_add_new_set()
    {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        //training list view visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.training_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //set list view visible
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_set)).perform(click()).also { Thread.sleep(500) }

        onView(withId(R.id.add_set_layout)).check(matches(isDisplayed()))

        //fill data
        onView(withId(R.id.add_set_exercise_name)).perform(typeText("test1"))
        onData(equalTo("Test1")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        closeSoftKeyboard()

        //go back to set list
        onView(withId(R.id.add_set_fab)).perform(click())
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))

    }

    @Test
    fun test_navigation_training_details_exercise_details_and_back()
    {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        //training list view visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.training_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //set list view visible
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.set_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //exercise details view visible
        onView(withId(R.id.exercise_details_layout)).check(matches(isDisplayed()))

        //going back to set list
        pressBack()
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))

        //going back to training list
        pressBack()
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
    }
}