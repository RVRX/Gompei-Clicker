package com.colermanning.gompeiclicker.api

import retrofit2.Call
import retrofit2.http.GET

interface OpenWeatherApi {
    @GET("weather?q=Worcester&appid=688af00a33f7b92e75e557583bc4dc1f") //GET request
    fun fetchContents(): Call<String> //deserialize http response to string
}