package com.example.sonatale

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sonatale.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent
    private var mediaPlayer: MediaPlayer? = null
    private var isListening = false
    private var isRunning = true

    private val handler = Handler(Looper.getMainLooper())
    private var bookTitle: String = ""
    private val playedUris = mutableListOf<String>()

    private var idleHandler = Handler(Looper.getMainLooper())
    private var idleRunnable: Runnable? = null
    private var lastRecognizedText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speechRecognize()
        onClickListener()
        setBookTitle()
    }

    private fun onClickListener() {
        binding.ivRecordOff.setOnClickListener {
            if (!isListening) {
                isListening = true
                isRunning = true
                startPeriodicSTT()
                binding.ivRecordOff.visibility = View.INVISIBLE
                binding.ivRecordOn.visibility = View.VISIBLE
            }
        }

        binding.ivRecordOn.setOnClickListener {
            if (isListening) {
                isListening = false
                isRunning = false
                stopListening()
                binding.ivRecordOff.visibility = View.VISIBLE
                binding.ivRecordOn.visibility = View.INVISIBLE
            }
        }
    }

    private fun speechRecognize() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000)
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Log.d("STT", "음성 인식 오류 발생: $error")
            }

            override fun onResults(results: Bundle?) {}

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val partialText = matches[0]
                    binding.tvResult.text = partialText
                    lastRecognizedText = partialText
                    resetIdleTimer()
                    Log.d("STT", "부분 인식된 텍스트: $partialText")
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun resetIdleTimer() {
        idleRunnable?.let { idleHandler.removeCallbacks(it) }

        idleRunnable = Runnable {
            if (lastRecognizedText.isNotBlank()) {
                lastRecognizedText = ""
            }
        }

        idleHandler.postDelayed(idleRunnable!!, 2000L)
    }

    private fun getMusicUrlForEmotion(emotion: String) {
        val api = getRetrofit().create(MainInterface::class.java)

        api.getTTSList().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    val musicMap = response.body()
                    val relativePath = musicMap?.get(emotion) ?: return
                    val fullUrl = "https://your.server.domain/$relativePath" // 실제 서버 URL로 수정
                    playMusicFromUrl(fullUrl)
                    playedUris.add(fullUrl)
                    restartListening()
                } else {
                    Log.e("MusicListAPI", "목록 응답 실패")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("MusicListAPI", "API 호출 실패: ${t.message}")
            }
        })
    }

    private fun playMusicFromUrl(url: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener { release() }
        }
    }

    private fun restartListening() {
        if (isListening) {
            speechRecognizer.stopListening()
            speechRecognizer.startListening(speechRecognizerIntent)
        }
    }

    private fun stopListening() {
        if (isListening) {
            isListening = false
            isRunning = false
            speechRecognizer.stopListening()
        }
    }

    private fun setBookTitle() {
        binding.btnDone.setOnClickListener {
            bookTitle = binding.etTitle.text.toString()
            if (bookTitle.isNotBlank()) {
                binding.layoutTitle.visibility = View.GONE
                binding.ivRecordOff.visibility = View.VISIBLE
                binding.infoRecord.visibility = View.VISIBLE
            }
        }
    }

    private fun startPeriodicSTT() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning) {
                    speechRecognizer.startListening(speechRecognizerIntent)
                    handler.postDelayed(this, 25000L)
                }
            }
        }, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        speechRecognizer.destroy()
    }
}
