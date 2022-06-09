package com.example.capstonedermapps

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.capstonedermapps.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityRegisterBinding.root)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USER")

        
        activityRegisterBinding.btnRegis.setOnClickListener{
            val email = activityRegisterBinding.etEmail.text.toString().trim()
            val pass = activityRegisterBinding.etPassword.text.toString().trim()
            val nama = activityRegisterBinding.etName.text.toString().trim()
            val age = activityRegisterBinding.etAge.text.toString().trim()
            val gender = activityRegisterBinding.etGender.text.toString().trim()
            
            if (checkValidation(email, pass,nama,age,gender )){
                registerToServer(email,pass,nama,age,gender)
            }
        }
    }

    private fun checkValidation(email: String, pass: String, nama: String, age: String, gender: String): Boolean {
        if (email.isEmpty()){
            activityRegisterBinding.etEmail.error = "Please field your email"
            activityRegisterBinding.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            activityRegisterBinding.etEmail.error = "Please use valid email"
            activityRegisterBinding.etEmail.requestFocus()
        } else if(pass.isEmpty()){
            activityRegisterBinding.etPassword.error="Please field your password"
            activityRegisterBinding.etPassword.requestFocus()
        }else if(nama.isEmpty()) {
            activityRegisterBinding.etName.error = "Please field your name"
            activityRegisterBinding.etName.requestFocus()
        }else if(age.isEmpty()) {
            activityRegisterBinding.etAge.error = "Please field your age"
            activityRegisterBinding.etAge.requestFocus()
        }else if(gender.isEmpty()) {
            activityRegisterBinding.etGender.error = "Please field your gender"
            activityRegisterBinding.etGender.requestFocus()
        }else{
            activityRegisterBinding.etPassword.error = null
            return true
        }
        return false
    }

    private fun registerToServer(email:String, pass: String, nama: String, age: String, gender: String) {
        val progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog.setTitle("Registrasi User")
        progressDialog.setMessage("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUser(email,nama,age,gender,progressDialog)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Register is Failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveUser(nama: String, email: String, age: String, gender: String, progressDialog: ProgressDialog){
        val currentUserId =auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("USER")
        val userMap = HashMap<String,Any>()
        userMap["id"] =  currentUserId
        userMap["email"] = email
        userMap["nama"] = nama
        userMap["age"] = age
        userMap["gender"] = gender

        ref.child(currentUserId).setValue(userMap).addOnCompleteListener{
            if(it.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this, "register is succesfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }else{
                val message = it.exception!!.toString()
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }

    }
}