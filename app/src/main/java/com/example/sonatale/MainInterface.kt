package com.example.sonatale

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainInterface {
    @POST("/api/v1/process_app/")
    fun translate(@Body translateRequest: TranslateRequest): Call<TranslateResponse>
}