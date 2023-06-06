package com.example.garudabirdapp

import android.content.Context
import android.widget.Toast

class Utility {

    companion object{

        fun showToast(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}