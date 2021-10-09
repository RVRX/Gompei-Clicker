package com.colermanning.gompeiclicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colermanning.gompeiclicker.api.OpenWeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "WeatherChecker"

class WeatherChecker {

    private val openWeatherApi: OpenWeatherApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/") //openWeather endpoint
            .addConverterFactory(ScalarsConverterFactory.create()) //scalar converter factory to deserialize to string
            .build()
        openWeatherApi = retrofit.create(OpenWeatherApi::class.java) //create instance of the API
    }

    fun fetchContents(): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = openWeatherApi.fetchContents()
        flickrRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ){
                Log.d(TAG, "Response received")
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }
}