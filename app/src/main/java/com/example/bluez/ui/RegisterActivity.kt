package com.example.bluez.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bluez.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("USERS")

        binding.btnRegister.setOnClickListener {
            val name = binding.txtInputName.text.toString().trim()
            val emails = binding.txtInputEmail.text.toString().trim()
            val password =  binding.txtInputPassword.text.toString().trim()
            val confirmPassword = binding.txtInputConfirmPassword.text.toString().trim()
            val username = binding.txtInputName.text.toString().trim()
            val email = binding.txtInputEmail.text.toString().trim()
            val phone = binding.txtInputPhone.text.toString().trim()


            when {
                name.isEmpty() -> {
                    binding.txtInputName.error = "Enter Your Name!"
                    binding.txtInputName.requestFocus()
                    return@setOnClickListener
                }
                emails.isEmpty() -> {
                    binding.txtInputEmail.error = "Enter Your Email!"
                    binding.txtInputEmail.requestFocus()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.txtInputPassword.error = "Enter Your Password!"
                    binding.txtInputPassword.requestFocus()
                    return@setOnClickListener
                }
                confirmPassword != password -> {
                    binding.txtInputConfirmPassword.error = "Confirm Password Don`t Match!"
                    binding.txtInputConfirmPassword.requestFocus()
                    return@setOnClickListener
                }
                username.isEmpty() -> {
                    binding.txtInputName.error = "Username required!"
                    binding.txtInputName.requestFocus()
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.txtInputEmail.error = "Email Required!"
                    binding.txtInputEmail.requestFocus()
                    return@setOnClickListener
                }
                phone.isEmpty() -> {
                    binding.txtInputPhone.error = "Phone Number Required!"
                    binding.txtInputPhone.requestFocus()
                    return@setOnClickListener
                }
            }
            registerUser(emails,password,username,phone)
        }

        binding.txtHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String, username: String, phoneNumber: String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Registaring User")
        progressDialog.setMessage("Please Wait!")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
//                    Toast.makeText(applicationContext,"Registration Successful",Toast.LENGTH_LONG).show()
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    finish()
                    saveUserInfo(email,username,phoneNumber, progressDialog)
                } else{
                    Toast.makeText(applicationContext,"Registration Failed!", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserInfo(email: String, username:String, phoneNumber: String ,progressDialog: ProgressDialog,){
        val currentUserId = mAuth.currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("USERS")
        val userMap = HashMap<String,Any>()
        userMap["id"] = currentUserId
        userMap["email"] = email
        userMap["username"] = username
        userMap["phoneNumber"] = phoneNumber

        databaseReference.child(currentUserId).setValue(userMap).addOnCompleteListener {
            if (it.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Registration Successfull", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext,"Registration Failed!", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }
    }
}