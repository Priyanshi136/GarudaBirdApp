package com.example.garudabirdapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import java.io.ByteArrayOutputStream

class ImageIdentify : AppCompatActivity() {

    lateinit var imgView: ImageView
    lateinit var img_ident: Button
    lateinit var relati_vity: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_identify)

        imgView = findViewById(R.id.imageView)
        img_ident = findViewById(R.id.identify)
        relati_vity = findViewById(R.id.relativity)

        val byteArray = intent.getByteArrayExtra("image")
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!)

        imgView.setImageBitmap(bitmap)
        img_ident.setOnClickListener {
            imgView.visibility = View.GONE
            img_ident.visibility = View.GONE
            relati_vity.visibility = View.VISIBLE
            // intent
            val intent = Intent(this, ImageOutput::class.java)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray2 = stream.toByteArray()
            intent.putExtra("image",byteArray2)
            startActivity(intent)
        }

    }
}