package com.example.sonatale

import com.example.sonatale.data.music.MusicRequest
import com.example.sonatale.data.music.MusicResponse
import com.example.sonatale.data.translate.TranslateRequest
import com.example.sonatale.data.translate.TranslateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainInterface {

    @GET("/tts/play/")
    fun musicGet(@Query(value = "text") text: String?): Call<MusicResponse>

    @GET("/tts/list")
    fun getTTSList(): Call<Map<String, String>>

    @POST("/api/music")
    fun postMusicGen(@Body request: MusicRequest): Call<MusicResponse>
}