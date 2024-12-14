package com.example.sonatale

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.NotificationCompat

class Foreground : Service() {

    private var intent: Intent? = null
    private var speechRecognizer: SpeechRecognizer? = null
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }

        createNotification()
        startSTT()
    }

    private fun startSTT() {
        stopSTT()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(listener)
            startListening(intent)
        }
    }

    private val listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {

        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onBufferReceived(buffer: ByteArray?) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(error: Int) {

        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.let {
                // 음성 인식 결과 로그 출력
                Log.d("결과 보여줘요", "음성 인식 결과: ${matches[0]}")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {

        }

        override fun onEvent(eventType: Int, params: Bundle?) {

        }
    }

    private fun stopSTT() {
        speechRecognizer?.apply {
            stopListening()
            destroy()
        }
        speechRecognizer = null
    }

    private fun createNotification() {
//        val builder = NotificationCompat.Builder(this, "default")
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setContentTitle("STT 변환")
//        builder.setContentText("음성 인식 중...")
//        builder.color = Color.RED
//        val notificationIntent = Intent(this, MainActivity::class.java)
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//        // 알림 클릭 시 이동
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
//        builder.setContentIntent(pendingIntent)
//
//        // 알림 표시
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "default",
//                "기본 채널",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//
//            builder.setChannelId("default")
//        }
        val builder = NotificationCompat.Builder(this, "default").apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("STT 변환")
            setContentText("음성 인식 중...")
            color = Color.RED
        }

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "기본 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            builder.setChannelId("default")
        }

        startForeground(NOTI_ID, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (speechRecognizer != null) {
//            speechRecognizer!!.stopListening()
//            speechRecognizer!!.destroy()
//            speechRecognizer = null
//        }

        stopSTT()
    }

    companion object {
        private const val TAG = "ForegroundTag"

        // Notification
        private const val NOTI_ID = 1
    }
}