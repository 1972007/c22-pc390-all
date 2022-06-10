package com.example.capstonedermapps

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        ref = FirebaseDatabase.getInstance().getReference("USERS")


        
        activityRegisterBinding.btnRegis.setOnClickListener{
            val email = activityRegisterBinding.etEmail.text.toString().trim()
            val pass = activityRegisterBinding.etPassword.text.toString().trim()
            val name = activityRegisterBinding.etName.text.toString().trim()
            val age = activityRegisterBinding.etAge.text.toString().trim()
            val gender = activityRegisterBinding.etGender.text.toString().trim()

            if(name.isEmpty()){
                activityRegisterBinding.etName.error = "Name required"
                activityRegisterBinding.etName.requestFocus()
                return@setOnClickListener
            }
            if(age.isEmpty()){
                activityRegisterBinding.etAge.error = "Age required"
                activityRegisterBinding.etAge.requestFocus()
                return@setOnClickListener
            }
            if(gender.isEmpty()){
                activityRegisterBinding.etGender.error = "Gender required"
                activityRegisterBinding.etGender.requestFocus()
                return@setOnClickListener
            }
            if(email.isEmpty()){
                activityRegisterBinding.etEmail.error = "Email required"
                activityRegisterBinding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if(pass.isEmpty()|| pass.length <6){
                activityRegisterBinding.etPassword.error = "Password required"
                activityRegisterBinding.etPassword.requestFocus()
                return@setOnClickListener
            }
            registerToServer( name, age, gender, email, pass)
        }
    }


    private fun registerToServer(name: String, age: String, gender: String, email:String, pass: String) {
        val progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog.setTitle("Registrasi User")
        progressDialog.setMessage("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, pass) .addOnCompleteListener (this){
            if(it.isSuccessful){
                saveUser(name, age, gender,email, progressDialog)
            }else{
                val message = it.exception!!.toString()
                Toast.makeText(this,"Error :$message", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUser(name: String,  age: String, gender: String,email: String, progressDialog: ProgressDialog){
        val currentUserId =auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("USERS")
        val userMap = HashMap<String,Any>()
        userMap["name"] = name
        userMap["age"] = age
        userMap["gender"] = gender
        userMap["id"] =  currentUserId
        userMap["email"] = email
        ref.child(currentUserId).setValue(userMap).addOnCompleteListener{
            if(it.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this, "register is succesfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                val message = it.exception!!.toString()
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }

    }
}