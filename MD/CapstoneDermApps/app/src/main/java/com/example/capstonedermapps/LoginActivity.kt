package com.example.capstonedermapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.capstonedermapps.databinding.ActivityLoginBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityLoginBinding.root)

        initFirebaseAuth()

        activityLoginBinding.btnLogin.setOnClickListener{
            val email = activityLoginBinding.etEmail.text.toString().trim()
            val pass = activityLoginBinding.etPass.text.toString().trim()

            if (checkValidation(email, pass)){
                loginToServer(email,pass)
            }
        }

        activityLoginBinding.lupaPass.setOnClickListener{
            startActivity(Intent(this,ActivityLupaPass::class.java))
        }

        activityLoginBinding.punyaAkun.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }

    private fun loginToServer(email: String, pass: String) {
        val credential = EmailAuthProvider.getCredential(email,pass)
        fireBaseAuth(credential)

    }

    private fun fireBaseAuth(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, TermsAndConditionsActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show() }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if(email.isEmpty()){
            activityLoginBinding.etEmail.error = "Please field your email"
            activityLoginBinding.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            activityLoginBinding.etEmail.error = "Please use valid email"
            activityLoginBinding.etEmail.requestFocus()
        }else if(pass.isEmpty()){
            activityLoginBinding.etPass.error="Please field your password"
            activityLoginBinding.etPass.requestFocus()
        }else{
            return true
        }
        return false
    }



}