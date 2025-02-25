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
        initBottomNavigation()
        setContentView(binding.root)

//        checkRecordPermission()
//
//        binding.ivRecordOff.setOnClickListener {
//            if (!isForegroundServiceRunning()) { // Foreground 서비스 실행 여부 체크
//                Toast.makeText(this, "음성인식 시작", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, Foreground::class.java)
//                startService(intent)
//            }
//
//            binding.ivRecordOff.visibility = View.INVISIBLE
//            binding.ivRecordOn.visibility = View.VISIBLE
//        }
//
//        binding.ivRecordOn.setOnClickListener {
//            Toast.makeText(this@MainActivity, "음성인식 종료", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@MainActivity, Foreground::class.java)
//            stopService(intent)
//
//            binding.ivRecordOff.visibility = View.VISIBLE
//            binding.ivRecordOn.visibility = View.INVISIBLE
//        }
    }

//    private fun speechToText() {
//        resultTextView = binding.tvResult
//
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
//
//        speechRecognizer.setRecognitionListener(object : RecognitionListener {
//            override fun onReadyForSpeech(params: Bundle?) {
//                Toast.makeText(this@MainActivity, "음성 인식을 시작합니다.", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onBeginningOfSpeech() {
//                // 사용자가 말하기 시작했을 때 호출
//            }
//
//            override fun onRmsChanged(rmsdB: Float) {
//                // 음성 입력의 소리 크기가 변경될 때 호출
//            }
//
//            override fun onBufferReceived(buffer: ByteArray?) {
//                // 음성 데이터를 받았을 때 호출
//            }
//
//            override fun onEndOfSpeech() {
//                Toast.makeText(this@MainActivity, "음성 입력이 끝났습니다.", Toast.LENGTH_SHORT).show()
//                binding.ivRecordOff.visibility = View.VISIBLE
//                binding.ivRecordOn.visibility = View.INVISIBLE
//            }
//
//            override fun onError(error: Int) {
//                val message: String = when (error) {
//                    SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
//                    SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
//                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
//                    SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
//                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 타임아웃"
//                    SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
//                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
//                    SpeechRecognizer.ERROR_SERVER -> "서버 에러"
//                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "시간 초과"
//                    else -> "알 수 없는 오류"
//                }
//                Toast.makeText(this@MainActivity, "Error : $message", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResults(results: Bundle?) {
//                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                Log.d("SpeechText", matches?.get(0).toString())
//                postTranslate(matches?.get(0))
//                if (!matches.isNullOrEmpty()) {
//                    resultTextView.text = translateText // 첫 번째 결과 표시
//                }
//            }
//
//            override fun onPartialResults(partialResults: Bundle?) {
//                // 부분 결과 처리
//            }
//
//            override fun onEvent(eventType: Int, params: Bundle?) {
//                // 추가 이벤트 처리
//            }
//        })
//
//        binding.ivRecordOff.setOnClickListener {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//                putExtra(
//                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//                )
//                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//                putExtra(RecognizerIntent.EXTRA_PROMPT, "말씀하세요...")
//            }
//            speechRecognizer.startListening(intent)
//
//            binding.ivRecordOff.visibility = View.INVISIBLE
//            binding.ivRecordOn.visibility = View.VISIBLE
//        }
//    }

    // 음성 권한 처리
//    private fun checkRecordPermission() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.RECORD_AUDIO),
//                1
//            )
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        speechRecognizer.destroy() // SpeechRecognizer 리소스 해제
//    }

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

//    private fun isForegroundServiceRunning(): Boolean {
//        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
//        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
//            if (Foreground::class.java.name == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }

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