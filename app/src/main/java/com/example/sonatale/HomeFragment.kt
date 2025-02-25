package com.example.sonatale

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sonatale.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    private var isListening = false // 음성 인식 상태 플래그

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speechRecognize()

        onClickListener()
    }

    private fun onClickListener() {
        binding.ivRecordOff.setOnClickListener {
            if (!isListening) {
                startListening()

                binding.ivRecordOff.visibility = View.INVISIBLE
                binding.ivRecordOn.visibility = View.VISIBLE

                isListening = true
            }
        }

        binding.ivRecordOn.setOnClickListener {
            stopListening()

            binding.ivRecordOff.visibility = View.VISIBLE
            binding.ivRecordOn.visibility = View.INVISIBLE

            isListening = false
        }
    }

    private fun speechRecognize() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())

        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 설정
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // 부분 결과 활성화
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000) // 무음 감지 시간 설정
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // 음성 인식 준비 완료
            }

            override fun onBeginningOfSpeech() {
                // 사용자가 말하기 시작 했을 때
            }

            override fun onRmsChanged(rmsdB: Float) {
                // 음성 입력의 소리 크기가 변경될 때
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // 음성 데이터를 받았을 때
            }

            override fun onEndOfSpeech() {
                // 음성 인식이 끝났을 때
            }

            // 음성 인식 오류 발생
            override fun onError(error: Int) {

                val message: String = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                    SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                    SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 타임아웃"
                    SpeechRecognizer.ERROR_NO_MATCH -> "적당한 결과를 찾을 수 없음"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER가 바쁨"
                    SpeechRecognizer.ERROR_SERVER -> "서버 에러"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "시간 초과"
                    else -> "알 수 없는 오류"
                }

                Log.d("STT", "음성 인식 오류 발생: $message")

                isListening = false

                restartListening()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getString(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    binding.tvResult.text = recognizedText.toString()
                    Log.d("STT", "음성 인식 텍스트: $recognizedText")
                }
                restartListening() // 인식 완료 후 다시 실행
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // 부분 결과 처리
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val partialText = matches[0]
                    binding.tvResult.text = partialText // 실시간으로 UI 업데이트
                    Log.d("STT", "부분 인식된 텍스트: $partialText")
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // 추가 이벤트 처리
            }

        })
    }

    // 음성 인식 시작
    private fun startListening() {
        if (!isListening) {
            isListening = true
            speechRecognizer.startListening(speechRecognizerIntent)
            Log.d("STT", "음성 인식 시작")
        }
    }

    // 음성 인식 재시작
    private fun restartListening() {
        if (isListening) {
            speechRecognizer.stopListening() // 현재 인식 종료
            speechRecognizer.startListening(speechRecognizerIntent) // 다시 시작
            Log.d("STT", "음성 인식 재시작")
        }
    }

    // 음성 인식 중지
    private fun stopListening() {
        if (isListening) {
            speechRecognizer.stopListening()
            Log.d("STT", "음성 인식 중지")
            isListening = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}