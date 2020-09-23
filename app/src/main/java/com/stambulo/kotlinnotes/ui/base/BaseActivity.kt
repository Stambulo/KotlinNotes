package com.stambulo.kotlinnotes.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.stambulo.kotlinnotes.R
import com.stambulo.kotlinnotes.data.errors.NoAuthException


abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 4242
    }

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
        viewModel.getViewState().observe(this, Observer { state ->
            state ?: return@Observer
            state.error?.let {
                renderError(it)
                return@Observer
            }
            renderData(state.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable){
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let {
                showError(it)
            }
        }
    }

    private fun startLogin() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.android_robot)
            .setTheme(R.style.LoginStyle)
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    protected fun showError(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}