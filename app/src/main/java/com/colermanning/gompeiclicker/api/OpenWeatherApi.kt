package com.colermanning.gompeiclicker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather?units=imperial&appid=688af00a33f7b92e75e557583bc4dc1f&format=json&nojsoncallback=1") //GET request
    fun fetchContents(
        @Query("lat") lat: String, //     add
        @Query("lon") lon: String  // query strings
    ): Call<String> //deserialize http response to string
}