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
    }
}