package com.colermanning.gompeiclicker

import android.app.Application

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