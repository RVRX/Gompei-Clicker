package com.colermanning.gompeiclicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colermanning.gompeiclicker.api.OpenWeatherApi
import org.json.JSONObject
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

    fun fetchContents(lat: String = "42.3334", lon: String = "-71.8328"): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val weatherRequest: Call<String> = openWeatherApi.fetchContents(lat, lon) //lat and lon
        weatherRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ){
                Log.d(TAG, "Response received")
                responseLiveData.value = parseResponse(response)
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

    /**
     * Parses the response to get the current weather type
     * @return one of "Clouds", "Clear", "Rain, "Snow". Defaults to "Clear" if input is bad
     * todo haven't verified snow as a return
     */
    fun parseResponse(response: Response<String>): String {
        val resBody = response.body()
        var weatherStatus = "Clear" //default

        if (resBody != null && resBody.contains("description")) { //!null and contains the correct main object
            val afterMain = resBody.substring((resBody.indexOf("main") + 7)) //after "main":
            weatherStatus = afterMain.substring(0,afterMain.indexOf("\"")) //till next quote
        }


        return weatherStatus;
    }
}