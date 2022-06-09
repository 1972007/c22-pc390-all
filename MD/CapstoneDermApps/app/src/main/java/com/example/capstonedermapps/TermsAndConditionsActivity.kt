package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstonedermapps.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var activityTermsAndConditionsBinding : ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTermsAndConditionsBinding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(activityTermsAndConditionsBinding.root)

        activityTermsAndConditionsBinding.btnSetuju.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}