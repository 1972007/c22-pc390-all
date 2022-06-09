package com.example.capstonedermapps

import android.Manifest
import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capstonedermapps.databinding.ActivityScanBinding
import com.example.capstonedermapps.helper.Helper
import java.io.File

class ScanActivity : AppCompatActivity() {
    private lateinit var activityScanBinding: ActivityScanBinding
    private var getFile: File? = null
    private var result: Bitmap? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityScanBinding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(activityScanBinding.root)

        //user = intent.getParcelableExtra(EXTRA_USER)!!

        getPermission()

        activityScanBinding.btnCameraX.setOnClickListener { startCameraX() }
        activityScanBinding.btnGallery.setOnClickListener { startGallery() }
        activityScanBinding.btnHasil.setOnClickListener { startHasil() }

    }
    private fun getPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    // cameraX
    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(this, ActivityCamera::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
                Helper
                    .rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
        }
        activityScanBinding.ivPreview.setImageBitmap(result)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startHasil(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type ="image/*"
        startActivity(Intent(this, ActivityHasilScanning::class.java))

    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = Helper.uriToFile(selectedImg, this@ScanActivity)
            getFile = myFile
            activityScanBinding.ivPreview.setImageURI(selectedImg)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        const val EXTRA_USER = "user"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}