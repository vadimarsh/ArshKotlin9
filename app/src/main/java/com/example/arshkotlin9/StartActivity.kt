package com.example.arshkotlin9

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if (isAuthenticated()) {
            start<PostsActivity>()
            finish()
        } else {
            but_login.setOnClickListener {
                if (!isValid(et_password.text.toString()) || (et_password.text.toString()
                        .isEmpty())
                ) {
                    et_password.error = getString(R.string.msg_paswd_invalid_err)
                } else {
                    lifecycleScope.launch {
                        progressBar.isIndeterminate = true
                        progressBar.visibility = VISIBLE
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        try {
                            val response =
                                App.repository.authenticate(
                                    et_login.text.toString(),
                                    et_password.text.toString()
                                )

                            if (response.isSuccessful && response.code() != 400) {
                                toast(getString(R.string.msg_auth_succ))
                                setUserAuth(response.body()!!.token)

                                start<PostsActivity>()
                                finish()
                            } else {
                                toast(getString(R.string.msg_auth_err))
                            }
                        } catch (e: Exception) {
                            toast(getString(R.string.msg_connection_err))
                        }
                        progressBar.visibility = GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
                }
            }
            but_reg.setOnClickListener {
                start<RegistrationActivity>()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (isAuthenticated()) {
            start<PostsActivity>()
            finish()
        }
    }

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit {
                putString(AUTHENTICATED_SHARED_KEY, token)
            }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun cleanUserAuth() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).edit() {
            clear()
            //putString(AUTHENTICATED_SHARED_KEY, "")

        }
}