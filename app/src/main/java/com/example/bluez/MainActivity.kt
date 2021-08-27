package com.example.bluez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bluez.databinding.ActivityMainBinding
import com.example.bluez.databinding.ActivityRegisterBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.tvUserId.text = "USER ID :: $userId"
        binding.tvEmailId.text = "EMAIL :: $emailId"
    }
}