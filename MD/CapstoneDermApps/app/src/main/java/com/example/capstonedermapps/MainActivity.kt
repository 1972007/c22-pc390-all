package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.capstonedermapps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        activityMainBinding.cardScan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        activityMainBinding.cardWiki.setOnClickListener {
            startActivity(Intent(this, SkinWikiActivity::class.java))
        }
        activityMainBinding.cardAbout.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        activityMainBinding.linIcon.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.log_out_success))
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }

    }
}
