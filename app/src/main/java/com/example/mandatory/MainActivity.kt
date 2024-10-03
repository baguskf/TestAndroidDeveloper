package com.example.mandatory

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mandatory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MyService.ServiceCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inisialisasi service
        serviceIntent = Intent(this, MyService::class.java)

        (application as mandatory).callback = this


        binding.startServiceButton.setOnClickListener {
            startService(serviceIntent)
        }

        //notifikasi wajib diizinkan
        checkNotificationPermission()
    }


    override fun onDataReceived(data: String) {

        binding.displayTextView.text = data
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(serviceIntent)
        (application as mandatory).callback = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}
