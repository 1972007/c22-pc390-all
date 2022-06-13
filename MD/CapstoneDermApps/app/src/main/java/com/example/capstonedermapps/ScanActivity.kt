package com.example.capstonedermapps

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.example.capstonedermapps.ml.Ham10000Xception
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class ScanActivity : AppCompatActivity() {

    lateinit var bitmap : Bitmap
    lateinit var imgView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val fileName = "results.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        var townList = inputString.split("\n")


        imgView = findViewById(R.id.iv_preview)

        val camera: Button = findViewById(R.id.btn_Camera_X)
        camera.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)

        }

        val identify : Button = findViewById(R.id.btn_identify)

        identify.setOnClickListener {
            val model = Ham10000Xception.newInstance(this)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 600, 450, 3), DataType.FLOAT32)
            bitmap = Bitmap.createScaledBitmap(bitmap,600,450,true)
            var bitmap = imgView.getDrawable().toBitmap()
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            val byteBuffer = tensorImage.buffer
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max= getMax(outputFeature0.floatArray)

            val tvhasil = findViewById<TextView>(R.id.tv_hasil)
            tvhasil.text = townList[max]
            model.close()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imgView.setImageURI(data?.data)
        val uri: Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }

    fun getMax(arr : FloatArray): Int{
        var ind = 0
        var min = 0.0f

        for(i in 0..6){
            if(arr[i] > min){
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}
