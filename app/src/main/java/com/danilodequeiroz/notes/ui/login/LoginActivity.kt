package com.danilodequeiroz.notes.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.test.espresso.idling.CountingIdlingResource
import com.danilodequeiroz.notes.ui.MainActivity
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.login.state.ErrorState
import com.danilodequeiroz.notes.ui.login.state.FetchedLoginDataState
import com.danilodequeiroz.notes.ui.login.state.LoadingState
import com.danilodequeiroz.notes.ui.login.state.SuccessState
import com.danilodequeiroz.notes.ui.notelist.LoginDataClickLisneter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.main_activity.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.danilodequeiroz.notes.ui.widget.LoginDataAdapter
import com.danilodequeiroz.notes_persistence.LoginData
import androidx.test.espresso.IdlingResource
import androidx.annotation.VisibleForTesting


class LoginActivity : AppCompatActivity(), LoginDataClickLisneter {

    private val viewModel: LoginViewModel by viewModel()
    private var countingIdlingResource:CountingIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupViewEvents()

        viewModel.viewState().observe(this, Observer {
            ;
            when(it){
                is FetchedLoginDataState->{ setupAutoComplete(it.loginData) }
                is SuccessState->{startNotesList().run { countingIdlingResource?.decrement() }}
                is LoadingState->{showLoading()}
                is ErrorState->{hideLoading(); showFeedbackSnackBar(it.errorMessage).run { countingIdlingResource?.decrement() } }
            }
        })
    }

    override fun onLoginDataCicked(item: LoginData) {
        editTextEmail.setText(item.email)
        editTextPassword.setText(item.password)
        editTextEmail.dismissDropDown()
    }

    private fun setupAutoComplete(loginData: MutableList<LoginData>) {
        val adapter = LoginDataAdapter(this, R.layout.login_suggest_item, loginData,this)
        editTextEmail.threshold = 1 //will start working from first character

        editTextEmail.setAdapter(adapter)
    }

    private fun fieldsValid(): Boolean {
        val emailValid = if (editTextEmail.text.isNullOrEmpty()) {
            textInputEmail.error = getString(R.string.field_required)
            false
        } else {
            textInputEmail.error = null
            true
        }
        val passwordValid = if (editTextPassword.text.isNullOrEmpty()) {
            textInputPassword.error = getString(R.string.field_required)
            false
        } else {
            textInputPassword.error = null
            true
        }
        return (emailValid && passwordValid )
    }

    private fun setupViewEvents() {
        buttonSignIn.setOnClickListener {
            if (!fieldsValid())
                return@setOnClickListener
            viewModel.login(editTextEmail.text.toString(), editTextPassword.text.toString(),checkBoxKeepData.isChecked).run {  countingIdlingResource?.increment(); }
        }
    }

    fun startNotesList(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
        finish()
    }

    private fun hideLoading() {
        if (buttonSignIn.visibility == View.GONE && contentLoading.visibility == View.VISIBLE) {
            buttonSignIn.visibility = View.VISIBLE
            contentLoading.visibility = View.GONE
        }
    }

    private fun showLoading() {
        if (buttonSignIn.visibility == View.VISIBLE && contentLoading.visibility == View.GONE) {
            buttonSignIn.visibility = View.GONE
            contentLoading.visibility = View.VISIBLE
        }
    }
    private fun showFeedbackSnackBar(message: String?) {
        findViewById<View>(android.R.id.content)?.let { view ->
            message?.let { message->
                val snackBar = Snackbar.make(
                    view,
                    message,
                    Snackbar.LENGTH_LONG
                )
                snackBar.show()
            }
        }
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        if (countingIdlingResource == null) {
            countingIdlingResource = CountingIdlingResource("LoginActivity Network Call")
        }
        return countingIdlingResource as CountingIdlingResource
    }
}