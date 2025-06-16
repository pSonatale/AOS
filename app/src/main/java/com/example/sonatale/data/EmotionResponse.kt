package com.example.sonatale.data

data class EmotionResponse(
    val emotions: List<String>,
    val confidence: List<Float>
)
