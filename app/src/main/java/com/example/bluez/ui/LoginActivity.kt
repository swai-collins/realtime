package com.example.bluez.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.bluez.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.txtDontHave.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.txtForgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }


    private fun loginUser(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sign In User")
        progressDialog.setMessage("Please Wait!")
        progressDialog.setCanceledOnTouchOutside(true)
        progressDialog.show()
        val email: String =  binding.txtEnterEmail.text.toString().trim()
        val password: String = binding.txtEnterPassword.text.toString().trim()

        if (TextUtils.isEmpty(email)){
            binding.txtEnterEmail.error = "Enter Email!"
            return
        } else if (TextUtils.isEmpty(password)){
            binding.txtEnterPassword.error = "Enter Your Password!"
            return
        }
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Successfully Signed In",Toast.LENGTH_LONG).show()
                    var intent = Intent(applicationContext,ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Sign In Failed!", Toast.LENGTH_LONG).show()
                }
            }
    }
}