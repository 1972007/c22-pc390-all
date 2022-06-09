package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstonedermapps.databinding.ActivityHasilScanningBinding

class ActivityHasilScanning : AppCompatActivity() {

    private lateinit var activityHasilScanningBinding : ActivityHasilScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHasilScanningBinding = ActivityHasilScanningBinding.inflate(layoutInflater)
        setContentView(activityHasilScanningBinding.root)

        activityHasilScanningBinding.vback.setOnClickListener{
            startActivity(Intent(this, ScanActivity::class.java))
        }
    }
}