package com.danilodequeiroz.notes.custommatcher

import android.view.View

import com.google.android.material.textfield.TextInputLayout

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.danilodequeiroz.notes.ui.widget.NotesRecyclerViewAdapter


fun hasInputError(expectedErrorText: String?): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout) {
                return false
            }
            val error = view.error ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }

        override fun describeTo(description: Description) {
            description.appendText("input does note have errors")
        }
    }
}

fun hasNoInputError(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        var error :CharSequence? = null
        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout) {
                return false
            }

            error = view.error ?: return true
            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("input does have errors: $error")
        }
    }
}

fun withTitle(title: String): Matcher<RecyclerView.ViewHolder> {
    return object :
        BoundedMatcher<RecyclerView.ViewHolder, NotesRecyclerViewAdapter.MyViewHolder>(NotesRecyclerViewAdapter.MyViewHolder::class.java) {
        override fun matchesSafely(item: NotesRecyclerViewAdapter.MyViewHolder): Boolean {
            return item.title.text.toString().toLowerCase() == title.toLowerCase()
        }

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $title")
        }
    }
}

fun withDescription(description: String): Matcher<RecyclerView.ViewHolder> {
    return object :
        BoundedMatcher<RecyclerView.ViewHolder, NotesRecyclerViewAdapter.MyViewHolder>(NotesRecyclerViewAdapter.MyViewHolder::class.java) {
        override fun matchesSafely(item: NotesRecyclerViewAdapter.MyViewHolder): Boolean {
            return item.description.text.toString().toLowerCase() == description.toLowerCase()
        }

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $description")
        }
    }
}

fun withPriority(priority: String): Matcher<RecyclerView.ViewHolder> {
    return object :
        BoundedMatcher<RecyclerView.ViewHolder, NotesRecyclerViewAdapter.MyViewHolder>(NotesRecyclerViewAdapter.MyViewHolder::class.java) {
        override fun matchesSafely(item: NotesRecyclerViewAdapter.MyViewHolder): Boolean {
            return item.priority.text.toString().toLowerCase() == priority.toLowerCase()
        }

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $priority")
        }
    }
}
