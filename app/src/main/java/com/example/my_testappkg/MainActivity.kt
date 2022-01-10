package com.example.my_testappkg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_testappkg.databinding.ActivityMainBinding
import com.example.my_testappkg.databinding.FragmentMainBinding
import com.example.my_testappkg.extention.viewBinding
import com.example.my_testappkg.presentation.main.MainFragment
import com.example.my_testappkg.presentation.weather.WeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    private var currentItem = R.id.item_home
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.navigation.setOnNavigationItemSelectedListener(this)
        binding.navigation.itemIconTintList = null
        if (savedInstanceState == null) {
            loadFragment(MainFragment.newInstance())
        }
        setupBroadcastListener()

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //clear all fragment back stack
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.item_home -> if (currentItem != item.itemId) {
                item.setIcon(R.drawable.icon_home)
                fragment = MainFragment.newInstance()
            }
            R.id.item_weather -> if (currentItem != item.itemId) {
                item.setIcon(R.drawable.icon_weather)
                fragment = WeatherFragment.newInstance()
            }
        }

        currentItem = item.itemId
        return loadFragment(fragment)
    }


    private var broadcastReceiver: BroadcastReceiver? = null
    private fun setupBroadcastListener() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action.equals(
                        "refresh_home",
                        ignoreCase = true
                    ) && currentItem == R.id.item_home
                )
                else {
                    loadFragment(MainFragment.newInstance())
                }
            }
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver!!, IntentFilter("update_cart"))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (supportFragmentManager.backStackEntryCount == 1 && currentItem == R.id.item_home) {
                sendBroadcast(Intent("refresh_home"))
            }
            supportFragmentManager.popBackStack()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true

            Toast.makeText(
                applicationContext,
                "Please click BACK again to exit",
                Toast.LENGTH_SHORT
            ).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    fun getNav(): BottomNavigationView {
        return binding.navigation
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commitAllowingStateLoss()
            return true
        }
        return false
    }

}