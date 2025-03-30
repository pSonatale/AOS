package com.example.sonatale

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sonatale.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var translateText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkRecordPermission()
        initBottomNavigation()

        setContentView(binding.root)
    }

    // 음성 권한 처리
    private fun checkRecordPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }
    }


    // 번역 api 연동
    private fun postTranslate(text: String?) {
        val mainService = getRetrofit().create(MainInterface::class.java)

        mainService.translate(TranslateRequest(text)).enqueue(object : Callback<TranslateResponse> {
            override fun onResponse(
                call: Call<TranslateResponse>,
                response: Response<TranslateResponse>
            ) {
                Log.d("Translate/ServerSuccess", response.message())
                Log.d("postTranslate", response.body()?.translatedText.toString())

                if (response.code() == 200) {
                    Log.d("Translate/Success", "TranslatePost")

                    translateText = response.body()?.translatedText.toString()
                }
            }

            override fun onFailure(call: Call<TranslateResponse>, t: Throwable) {
                Log.d("Translate/Failure", t.message.toString())
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거부되었습니다. 음성 인식이 제한됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNavBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.bottom_nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_nav_book -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, TaleFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}