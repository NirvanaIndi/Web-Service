package com.example.detectapp

import android.content.Context
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyRetrofit {
    @GET("history")
    fun history(): Call<List<History>>

//    @Headers({
//        "Accept: application/vnd.github.v3.full+json",
//        "User-Agent: Retrofit-Sample-App"
//    })
    @Multipart
    @POST("detection/doupload")
    fun detection(
        @Part image: MultipartBody.Part
    ): Call<History>

    companion object {
        public fun create(context: Context) : MyRetrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://v2.tegalian.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MyRetrofit::class.java)
        }
    }
}