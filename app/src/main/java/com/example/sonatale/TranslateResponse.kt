package com.example.sonatale

import com.google.gson.annotations.SerializedName

data class TranslateResponse (
    @SerializedName("translated_text")val translatedText: String?
)