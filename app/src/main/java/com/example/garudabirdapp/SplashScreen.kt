package com.example.garudabirdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(currentUser == null){
                startActivity(Intent(this, IntroScreen::class.java))
            }else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 1000)

        window.statusBarColor = this.resources.getColor(R.color.splash)
    }
}