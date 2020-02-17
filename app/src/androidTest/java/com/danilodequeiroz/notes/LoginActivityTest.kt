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
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun given_using_invalid_login_then_no_record_found_do_not_login() {
        activityScenarioRule.scenario.recreate()

        activityScenarioRule.scenario.onActivity { activity ->
            IdlingRegistry.getInstance().register(activity.getIdlingResource())
        }

        onView(withId(R.id.editTextEmail)).perform(replaceText("novousuario@email.com"))
        onView(withId(R.id.editTextPassword)).perform(replaceText("somkiat"), closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(containsString("There is no user record"))))
        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun given_using_valid_login_then_do_login() {
        activityScenarioRule.scenario.recreate()
        activityScenarioRule.scenario.onActivity { activity ->
            IdlingRegistry.getInstance().register(activity.getIdlingResource())
        }

        onView(withId(R.id.editTextEmail)).perform(replaceText("notaspessoais@desafioitau.com"))
        onView(withId(R.id.editTextPassword)).perform(replaceText("admin123"), closeSoftKeyboard())
        onView(withId(R.id.checkBoxKeepData)).perform(click())
        onView(withId(R.id.buttonSignIn)).perform(click())


        onView(withId(R.id.toolbar_title))
            .check(matches(withText(containsString("Notas"))))
        onView(withId(R.id.fabAddNote))
            .check(matches(isDisplayed()))
        onView(withId(R.id.menu_item_remove_all))
            .check(matches(isDisplayed()))
        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }


    @Test
    fun given_no_email_data_then_validates() {
        activityScenarioRule.scenario.recreate()
        activityScenarioRule.scenario.onActivity { activity ->
            IdlingRegistry.getInstance().register(activity.getIdlingResource())
        }

        onView(withId(R.id.editTextEmail)).perform(replaceText(""))
        onView(withId(R.id.editTextPassword)).perform(replaceText("uiyuiyiu"), closeSoftKeyboard())
        onView(withId(R.id.checkBoxKeepData)).perform(click())
        onView(withId(R.id.buttonSignIn)).perform(click())


        onView(withId(R.id.textInputEmail))
            .check(matches(hasInputError("Campo obrigatório")))
        onView(withId(R.id.textInputPassword))
            .check(matches(hasNoInputError()))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }


    @Test
    fun given_no_password_data_then_validates() {
        activityScenarioRule.scenario.recreate()
        activityScenarioRule.scenario.onActivity { activity ->
            IdlingRegistry.getInstance().register(activity.getIdlingResource())
        }

        onView(withId(R.id.editTextEmail)).perform(replaceText("notaspessoais@desafioitau.com"))
        onView(withId(R.id.editTextPassword)).perform(replaceText(""), closeSoftKeyboard())
        onView(withId(R.id.checkBoxKeepData)).perform(click())
        onView(withId(R.id.buttonSignIn)).perform(click())


        onView(withId(R.id.textInputEmail))
            .check(matches(hasNoInputError()))
        onView(withId(R.id.textInputPassword))
            .check(matches(hasInputError("Campo obrigatório")))

        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }


}