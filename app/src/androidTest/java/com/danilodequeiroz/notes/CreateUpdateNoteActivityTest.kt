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
import com.danilodequeiroz.notes.ui.login.LoginActivity
import com.danilodequeiroz.notes.ui.noteupdate.CreateUpdateNoteActivity
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreateUpdateNoteActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(CreateUpdateNoteActivity::class.java)

    @Test
    fun given_fill_a_note_without_priority_then_do_not_save() {
        activityScenarioRule.scenario.recreate()


        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descrição da nota"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())


        onView(withId(R.id.textInputTitle))
            .check(matches(hasNoInputError()))
        onView(withId(R.id.textInputDescription))
            .check(matches(hasNoInputError()))
        onView(withId(R.id.textInputPriority))
            .check(matches(hasInputError("Campo obrigatório")))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_fill_a_note_without_description_then_do_not_save() {
        activityScenarioRule.scenario.recreate()


        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())


        onView(withId(R.id.textInputTitle))
            .check(matches(hasNoInputError()))
        onView(withId(R.id.textInputDescription))
            .check(matches(hasInputError("Campo obrigatório")))
        onView(withId(R.id.textInputPriority))
            .check(matches(hasNoInputError()))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_fill_a_note_without_title_then_do_not_save() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())

        onView(withId(R.id.textInputTitle))
            .check(matches(hasInputError("Campo obrigatório")))
        onView(withId(R.id.textInputDescription))
            .check(matches(hasNoInputError()))
        onView(withId(R.id.textInputPriority))
            .check(matches(hasNoInputError()))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_fill_a_note_then_do_save() {
        activityScenarioRule.scenario.recreate()

        onView(withId(R.id.editTextTitle)).perform(replaceText("Titulo da nota"))
        onView(withId(R.id.editTextDescription)).perform(replaceText("Descricao da nota"))
        onView(withId(R.id.spinnerPriority)).perform(replaceText("1"), closeSoftKeyboard())
        onView(withId(R.id.fabUpdate)).perform(click())
        activityScenarioRule.scenario.onActivity {
            activity ->
            assert( activity.isFinishing)
        }
    }


}