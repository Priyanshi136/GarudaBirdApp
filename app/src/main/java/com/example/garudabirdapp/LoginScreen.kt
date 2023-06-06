package com.example.garudabirdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginAcc: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var createAcc: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        emailET = findViewById(R.id.email)
        passwordET = findViewById(R.id.password)
        createAcc = findViewById(R.id.createAccountBtn)
        progressBar = findViewById(R.id.progress_bar)
        loginAcc = findViewById(R.id.login_btn)

        firebaseAuth = FirebaseAuth.getInstance()

        loginAcc.setOnClickListener {
            loginUser()
        }
        createAcc.setOnClickListener {
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }

    private fun loginUser() {

        val email = emailET.text.toString()
        val password = passwordET.text.toString()

        val isValidated = validateData(email, password)
        if(!isValidated){
            return
        }

        loginAccountInFirebase(email, password)
    }

    private fun loginAccountInFirebase(email: String, password: String) {
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
            changeInProgress(false)
            if(task.isSuccessful){
                // login is success
                if(firebaseAuth.currentUser?.isEmailVerified == true){
                    // go to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Utility.showToast(this, "Email is not verified. Please verify it.")
                }
            }else{
                // login failed.
                task.exception?.localizedMessage?.let { Utility.showToast(this, it) }
            }
        }
    }

    private fun validateData(email: String, password: String): Boolean {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.error = "Email is invalid"
            return false
        }

        if(password.length < 6){
            passwordET.error = "Password length should be >6"
            return false
        }
        return true
    }

    private fun changeInProgress(inProgress: Boolean) {
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            loginAcc.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            loginAcc.visibility = View.VISIBLE
        }
    }
}