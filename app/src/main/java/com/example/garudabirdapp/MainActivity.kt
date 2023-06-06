package com.example.garudabirdapp

import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        replaceWithFragment(Home())

        bottomView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> replaceWithFragment(Home())
                R.id.item2 -> showDialog()
                R.id.item4 -> replaceWithFragment(Profile())
                else -> {

                }
            }
            true
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bird Identification")
        builder.setMessage("How do you want to proceed?")
        builder.setIcon(R.drawable.ic_baseline_location_searching_24)
        builder.setPositiveButton("Image Recog.", DialogInterface.OnClickListener { dialogInterface, i ->
            startActivity(Intent(this, ImageRecog::class.java))
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

        })
        builder.show()
    }

    private fun replaceWithFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}