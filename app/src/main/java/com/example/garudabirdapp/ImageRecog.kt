package com.example.garudabirdapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class ImageRecog : AppCompatActivity() {

    private lateinit var btnCapture: ImageView
    lateinit var btnLoad: Button
    private val GALLERY_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_recog)

        btnCapture = findViewById(R.id.takeImage)
        btnLoad = findViewById(R.id.loadImage)

        btnCapture.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
                takePicturePreview.launch(null)
            }else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        btnLoad.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg") // ???????????
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            }else{
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap->
        if(bitmap != null){
            val intent = Intent(this, ImageIdentify::class.java)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("image",byteArray)
            startActivity(intent)
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){granted->
        if(granted){
            takePicturePreview.launch(null)
        }else{
            Toast.makeText(this, "Permission Denied!! Try again", Toast.LENGTH_SHORT).show()
        }
    }

    private val onresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE, result)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult) {
        when(requestCode){
            GALLERY_REQUEST_CODE ->{
                if(result?.resultCode == Activity.RESULT_OK){
                    result.data?.data?.let { uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

                        val intent = Intent(this, ImageIdentify::class.java)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        intent.putExtra("image",byteArray)
                        startActivity(intent)
                    }
                }else{
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }
}