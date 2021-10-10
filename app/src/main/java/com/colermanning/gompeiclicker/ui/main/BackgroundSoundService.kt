package com.colermanning.gompeiclicker.ui.main

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import com.colermanning.gompeiclicker.R
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BackgroundSoundService : Service() {

    internal lateinit var player: MediaPlayer

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.theabacus);
        player.setLooping(true); // Set looping
        player.setVolume(100f,100f);
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.getAction().equals("PLAY")) {
            player.start()
        }
        if (intent.getAction().equals("PAUSE")) {
            player.pause()
        }
        return Service.START_STICKY;
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {

    }

    fun onPause() {

    }

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }
    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }
}