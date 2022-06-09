package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.capstonedermapps.databinding.ActivityLupaPassBinding
import com.google.firebase.auth.FirebaseAuth

class ActivityLupaPass : AppCompatActivity() {

    private lateinit var activityLupaPassBinding: ActivityLupaPassBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        activityLupaPassBinding = ActivityLupaPassBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityLupaPassBinding.root)

        activityLupaPassBinding.btnForgotpass.setOnClickListener{
            val email = activityLupaPassBinding.etEmail.text.toString().trim()
            if (email.isEmpty()){
                activityLupaPassBinding.etEmail.error="Pleasr field your email"
                activityLupaPassBinding.etEmail.requestFocus()
                return@setOnClickListener
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                activityLupaPassBinding.etEmail.error = "Please use valid email"
                activityLupaPassBinding.etEmail.requestFocus()
                return@setOnClickListener
            }else{
                forgotPassword(email)
            }
        }

    }

    private fun forgotPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Your reset password has been send to your email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }else{
                    Toast.makeText(this,"Failed reset password", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                exception -> Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

    }
}