package com.colermanning.gompeiclicker

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.colermanning.gompeiclicker.database.GompeiClickerDatabase

private const val TAG = "GompeiClickerApp"

class GompeiClickerApplication : Application() {

    /**
     * Called by the system when the application is first created,
     * Will init the DB repository.
     */
    override fun onCreate() {
        super.onCreate()
        GompeiClickerRepository.initialize(this)

        val gompeiClickerRepository = GompeiClickerRepository.get()
        val pointLiveData = gompeiClickerRepository.getPoints()

        if (pointLiveData.value == null) {
            Log.i(TAG, "DB Points == null, pre-populating DB...")
            //this is a new DB!
            // time to populate
            gompeiClickerRepository.populateDefaults()
        } else Log.d(TAG, "Existing DB, continuing with app start. Point Value: ${pointLiveData.value}")
    }
}