package com.example.garudabirdapp

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.garudabirdapp.ml.BirdsModel
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException

class ImageOutput : AppCompatActivity() {

    lateinit var result: TextView
    lateinit var birdImage: ImageView
    lateinit var downloadBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_output)

        result = findViewById(R.id.tvResult)
        birdImage = findViewById(R.id.birdImage)
        downloadBtn = findViewById(R.id.download)

        val byteArray2 = intent.getByteArrayExtra("image")
        val bitmap = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2?.size!!)

        outputGenerator(bitmap)
    }

    private fun outputGenerator(bitmap: Bitmap?) {

        // declaring tensor flow lite model variable
        val birdsModel = BirdsModel.newInstance(this)

        // Converting bitmap into tensorflow image
        val newBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
        val tfimage = TensorImage.fromBitmap(newBitmap)

        // Process the image using trained model and sort it in descending order
        val outputs = birdsModel.process(tfimage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }

        //getting result having high probability
        val highProbabilityOutput = outputs[0]

        //setting output text
        result.text = highProbabilityOutput.label
        birdImage.setImageBitmap(bitmap)

        result.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${result.text}"))
            startActivity(intent)
        }

        downloadBtn.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        Log.i("TAG", "outputGenerator: $highProbabilityOutput")

    }

    // to download image to device
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted: Boolean ->
        if(isGranted){
            AlertDialog.Builder(this).setTitle("Download Image?")
                .setMessage("Do you want to download this image to your device?")
                .setPositiveButton("Yes"){ _, _ ->
                    val drawable: BitmapDrawable = birdImage.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    downloadImage(bitmap)
                }
                .setNegativeButton("No"){dialog, l ->
                    dialog.dismiss()
                }
                .show()
        }else{
            Toast.makeText(this, "Please allow permission to download image", Toast.LENGTH_LONG).show()
        }
    }

    // function that takes a bitmap and store to user's device
    private fun downloadImage(mBitmap: Bitmap?):Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Birds_Image"+ System.currentTimeMillis()/1000)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        if(uri != null){
            contentResolver.insert(uri, contentValues)?.also {
                contentResolver.openOutputStream(it).use{ outputStream ->
                    if(!mBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)!!){
                        throw IOException("Couldn't save the bitmap")
                    }else{
                        Toast.makeText(applicationContext, "Image Saved", Toast.LENGTH_LONG).show()
                    }
                }
                return it
            }
        }
        return null
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ImageIdentify::class.java))
    }
}