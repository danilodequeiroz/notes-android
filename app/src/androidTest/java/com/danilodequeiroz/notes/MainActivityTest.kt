package com.danilodequeiroz.notes

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danilodequeiroz.notes.custommatcher.hasInputError
import com.danilodequeiroz.notes.custommatcher.hasNoInputError
import com.danilodequeiroz.notes.ui.MainActivity
import com.danilodequeiroz.notes.ui.login.LoginActivity
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.InstrumentationRegistry.getTargetContext
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import androidx.test.platform.app.InstrumentationRegistry
import com.danilodequeiroz.notes.custommatcher.withDescription
import com.danilodequeiroz.notes.custommatcher.withTitle
import org.junit.Before
import org.junit.BeforeClass




@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun beforeClass() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("Notes.db")
    }

    @Test
    fun given_opened_list_create_note_success() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }


    @Test
    fun given_opened_list_create_note_success_on_list() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())

        onView((withId(R.id.recyclerViewNotes))).
            perform(scrollToHolder(withTitle("Titulo da nota")),
                actionOnHolderItem(withTitle("Titulo da nota"), scrollTo())).check(
            matches(isDisplayed()))

        onView((withId(R.id.recyclerViewNotes))).
            perform(scrollToHolder(withTitle("Descricao da nota")),
                actionOnHolderItem(withTitle("Descricao da nota"), scrollTo())).check(
            matches(isDisplayed()))

        onView((withId(R.id.recyclerViewNotes))).
            perform(scrollToHolder(withTitle("Prioridade: 1")),
                actionOnHolderItem(withTitle("Prioridade: 1"), scrollTo())).check(
            matches(isDisplayed()))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_have_two_notes_delete_all() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())
        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota2"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota2"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())

        onView((withId(R.id.recyclerViewNotes))).
            perform(scrollToHolder(withTitle("Titulo da nota")),
                actionOnHolderItem(withTitle("Titulo da nota"), scrollTo())).check(
            matches(isDisplayed()))



        onView((withId(R.id.menu_item_remove_all))).perform(click())
        onView((withText("OK"))).perform(click())
        onView((withId(R.id.textViewEmpty))).check(matches(withText(containsString("Não há notas"))))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_have_two_notes_delete_one_by_swipe() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())
        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota2"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota2"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())

        onView((withId(R.id.recyclerViewNotes))).
            perform(scrollToHolder(withTitle("Titulo da nota")),
                actionOnHolderItem(withTitle("Titulo da nota"), swipeLeft()))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(containsString("Nota removida"))))


        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }


}