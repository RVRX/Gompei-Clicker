package com.colermanning.gompeiclicker.ui.main

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import com.colermanning.gompeiclicker.R
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.media.MediaPlayer.OnCompletionListener
import java.lang.Exception


class BackgroundSoundService : Service() {

    internal lateinit var player: MediaPlayer
    lateinit var playlist: ArrayList<Int>
    var songCount = 0;
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        playlist = ArrayList()
        playlist.add(R.raw.theabacus)
        playlist.add(R.raw.badapplewithout2or4)
        playlist.add(R.raw.lamar)
        player = MediaPlayer.create(this, playlist.get(0));
        //player.setLooping(true); // Set looping
        player.setVolume(100f,100f);
        player.setOnCompletionListener(OnCompletionListener { mp ->
            player.reset()
            if (songCount == playlist.size-1) {
                songCount = 0
            }
            else {
                songCount++
            }
            player = MediaPlayer.create(this, playlist.get(songCount))
            player.start()
        })
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.getAction().equals("PLAY")) {
            player.start()
        }
        if (intent.getAction().equals("PAUSE")) {
            player.pause()
        }

        if (intent.getAction().equals("NEXT")) {
            player.reset()
            if (songCount == playlist.size - 1) {
                songCount = 0
            } else {
                songCount++
            }
            player = MediaPlayer.create(this, playlist.get(songCount))
            player.start()
            player.setOnCompletionListener(OnCompletionListener { mp ->
                player.reset()
                if (songCount == playlist.size - 1) {
                    songCount = 0
                } else {
                    songCount++
                }
                player = MediaPlayer.create(this, playlist.get(songCount))
                player.start()
            })
        }

            if (intent.getAction().equals("PREVIOUS")) {
                player.reset()
                if (songCount == 0) {
                    songCount = playlist.size-1
                }
                else {
                    songCount--
                }
                player = MediaPlayer.create(this, playlist.get(songCount))
                player.start()
                player.setOnCompletionListener(OnCompletionListener { mp ->
                    player.reset()
                    if (songCount == playlist.size - 1) {
                        songCount = 0
                    } else {
                        songCount++
                    }
                    player = MediaPlayer.create(this, playlist.get(songCount))
                    player.start()
                })
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