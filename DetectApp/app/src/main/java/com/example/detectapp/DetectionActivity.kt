package com.example.detectapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import kotlinx.android.synthetic.main.activity_detection.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetectionActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection)
        setTitle("Detection")

        btn_camera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        Toast.makeText(this@DetectionActivity, "Create Image Unsuccessful", Toast.LENGTH_SHORT).show()
                        null
                    }

                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this@DetectionActivity,
                            "com.example.detectapp.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
            image_picture.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath))
//            Toast.makeText(this@DetectionActivity, currentPhotoPath, Toast.LENGTH_LONG).show()

            var bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            val width = 500 * bitmap.width / bitmap.height
            val height = 500
            bitmap = bitmap.scale(width, height, true)

            try {
                FileOutputStream(currentPhotoPath).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val imageFile = File(currentPhotoPath)
            val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
            MyRetrofit.create(this@DetectionActivity)
                .detection(multipartBody).enqueue(MyCallback(this@DetectionActivity, text_prediction))
            text_prediction.text = "Uploading...."
            Log.e("REST API", "Request")
        }
    }

    private class MyCallback(val context: Context, val prediction_text_view: TextView) :
        Callback<History> {
        override fun onResponse(call: Call<History>, response: Response<History>) {
            if (response.isSuccessful) {
                val history: History = response.body()!!
                prediction_text_view.text = history.prediction
                Toast.makeText(context, "l.", Toast.LENGTH_SHORT).show()

                Log.e("REST API", "Hasil History")
                Log.e("REST API", history.message)
            } else {
                Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show()
                Log.e("REST API", "Unsuccessful")
            }
        }

        override fun onFailure(call: Call<History>, t: Throwable) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            Log.e("REST API", "Failed")
        }
    }
}