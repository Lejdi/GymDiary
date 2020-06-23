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
        ActivityScenario.launch(MainActivity::class.java)
        //training list visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))

        //go to add new exercise
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Add exercise")).perform(click()).also { Thread.sleep(500) }
        onView(withId(R.id.exercise_details_layout)).check(matches(isDisplayed()))

        //fill exercise with data
        onView(withId(R.id.txt_exercisedetails_name_empty)).perform(typeText("espresso test"))
        closeSoftKeyboard()
        onView(withId(R.id.txt_exercisedetails_description)).perform(typeText("espresso test"))
        closeSoftKeyboard()
        onView(withId(R.id.txt_exercisedetails_rm)).perform(typeText("0"))
        closeSoftKeyboard()

        //save exercise
        onView(withId(R.id.btn_exercisedetails_save)).perform(click())
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigation_add_new_set()
    {
        ActivityScenario.launch(MainActivity::class.java)
        //training list view visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_traininglist)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //set list view visible
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_setlist_add)).perform(click()).also { Thread.sleep(500) }

        onView(withId(R.id.add_set_layout)).check(matches(isDisplayed()))

        //fill data
        onView(withId(R.id.txt_addset_exercisename)).perform(typeText("test1"))
        onData(equalTo("Test1")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        closeSoftKeyboard()

        //go back to set list
        onView(withId(R.id.btn_addset_save)).perform(click())
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))

    }

    @Test
    fun test_navigation_training_details_exercise_details_and_back()
    {
        ActivityScenario.launch(MainActivity::class.java)
        //training list view visible
        onView(withId(R.id.training_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_traininglist)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //set list view visible
        onView(withId(R.id.set_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_setlist)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

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