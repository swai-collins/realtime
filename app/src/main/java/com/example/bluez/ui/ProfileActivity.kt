package com.example.bluez.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.bluez.R
import com.example.bluez.databinding.ActivityProfileBinding
import com.example.bluez.databinding.ActivityRegisterBinding
import com.example.bluez.fragments.HomeFragment
import com.example.bluez.fragments.ProfileFragment
import com.example.bluez.fragments.RSVPFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment=HomeFragment()
        val rsvpFragment = RSVPFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homes->setCurrentFragment(homeFragment)
                R.id.rsvp->setCurrentFragment(rsvpFragment)
                R.id.profiles->setCurrentFragment(profileFragment)

            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout,fragment)
            commit()
        }
}