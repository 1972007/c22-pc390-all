package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.capstonedermapps.databinding.ActivityLoginBinding
import com.example.capstonedermapps.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var activityRegisterBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityRegisterBinding.root)
        
        activityRegisterBinding.btnRegis.setOnClickListener{
            val email = activityRegisterBinding.etEmail.text.toString().trim()
            val pass = activityRegisterBinding.etPassword.text.toString().trim()
            
            if (checkValidation(email, pass)){
                registerToServer(email,pass)
            }
        }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if (email.isEmpty()){
            activityRegisterBinding.etEmail.error = "Please field your email"
            activityRegisterBinding.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            activityRegisterBinding.etEmail.error = "Please use valid email"
            activityRegisterBinding.etEmail.requestFocus()
        }else if(pass.isEmpty()){
            activityRegisterBinding.etPassword.error="Please field your password"
            activityRegisterBinding.etPassword.requestFocus()
        }else{
            activityRegisterBinding.etPassword.error = null
            return true
        }
        return false
    }

    private fun registerToServer(email:String, pass: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Register Failed", Toast.LENGTH_SHORT).show()
            }
    }
}