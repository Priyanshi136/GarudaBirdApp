package com.example.garudabirdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class IntroScreen : AppCompatActivity() {

    private lateinit var loginBtn: MaterialButton
    private lateinit var guestBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)

        window.statusBarColor = this.resources.getColor(R.color.intro)

        loginBtn = findViewById(R.id.btnLog)
        guestBtn = findViewById(R.id.btnGuest)

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

        guestBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}