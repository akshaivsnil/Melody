package com.akshai.tutorials.melody.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.akshai.tutorials.melody.player.MediaPlayerHolder


/**
 * Created by ATM on 07/October/2022
 */

class MusicService : Service() {

    private val TAG = "MusicService"
    private val binder = LocalBinder()
    var mediaPlayerHolder: MediaPlayerHolder? = null

    inner class LocalBinder : Binder() {
        val instance: MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        if (mediaPlayerHolder == null){
            mediaPlayerHolder = MediaPlayerHolder(this)
        }
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerHolder!!.release()
    }
}