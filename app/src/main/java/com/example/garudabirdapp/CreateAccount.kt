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

class CreateAccount : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var confirmPassET: EditText
    private lateinit var createAcc: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginUser: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        emailET = findViewById(R.id.email)
        passwordET = findViewById(R.id.password)
        confirmPassET = findViewById(R.id.confirm_password)
        createAcc = findViewById(R.id.createAccount)
        progressBar = findViewById(R.id.progress_bar)
        loginUser = findViewById(R.id.loginBtn)

        firebaseAuth = FirebaseAuth.getInstance()

        createAcc.setOnClickListener {
            createAccount()
        }

        loginUser.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

    }

    private fun createAccount() {

        val email = emailET.text.toString()
        val password = passwordET.text.toString()
        val confirmPass = confirmPassET.text.toString()

        val isValidated = validateData(email, password, confirmPass)
        if(!isValidated){
            return
        }

        createAccountInFirebase(email, password)
        
    }

    private fun createAccountInFirebase(email: String, password: String) {

        changeInProgress(true)

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task->
            changeInProgress(false)
            if(task.isSuccessful){
                // creating account is done.
                Utility.showToast(this, "Successfully create account, Check email to verify")
                firebaseAuth.currentUser?.sendEmailVerification()
                firebaseAuth.signOut()
                finish()
            }else{
                // failure
                task.exception?.localizedMessage?.let { Utility.showToast(this, it) }
            }
        }
    }

    private fun changeInProgress(inProgress: Boolean) {
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            createAcc.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            createAcc.visibility = View.VISIBLE
        }
    }

    private fun validateData(email: String, password: String, confirmPass: String): Boolean {

        // validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.error = "Email is invalid"
            return false
        }

        if(password.length < 6){
            passwordET.error = "Password length should be >6"
            return false
        }

        if(password != confirmPass){
            confirmPassET.error = "Password not matched"
            return false
        }
        return true
    }
}