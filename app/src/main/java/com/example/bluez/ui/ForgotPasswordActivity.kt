package com.example.bluez.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.bluez.R
import com.example.bluez.databinding.ActivityForgotPasswordBinding
import com.example.bluez.databinding.ActivityForgotPasswordBinding.*
import com.example.bluez.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.btnLogins.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.btnGetLink.setOnClickListener {
            getEmailLink()
        }
    }
    private fun getEmailLink(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sending Link")
        progressDialog.setMessage("Please Wait!")
        progressDialog.setCanceledOnTouchOutside(true)
        progressDialog.show()
        val email: String = binding.inputEmailPassword.text.toString().trim()

        if (TextUtils.isEmpty(email)){
            binding.inputEmailPassword.error = "Enter Your Email!"
            return
        } else {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Check email to reset link!",Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this,"Email link not sent!", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}