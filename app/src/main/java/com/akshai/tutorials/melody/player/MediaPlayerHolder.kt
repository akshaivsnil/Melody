package com.akshai.tutorials.melody.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import com.akshai.tutorials.melody.adaptor.PlayerAdaptor
import com.akshai.tutorials.melody.model.Song
import com.akshai.tutorials.melody.service.MusicService


/**
 * Created by ATM on 07/October/2022
 */

class MediaPlayerHolder (private val musicService: MusicService) :
    MediaPlayer.OnPreparedListener , MediaPlayer.OnCompletionListener, PlayerAdaptor {

    private var mMediaPlayer: MediaPlayer? = null
    private var mContext : Context? = null
    private var mSelectedSong: Song? = null
    private var mSongs: List<Song>? = null

    init {
        mContext = musicService.applicationContext
    }

    override fun onPrepared(mp: MediaPlayer?) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun initMediaPlayer() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.reset()
            } else {
                mMediaPlayer = MediaPlayer()

                mMediaPlayer!!.setOnPreparedListener(this)
                mMediaPlayer!!.setOnCompletionListener(this)
                mMediaPlayer!!.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK)
                mMediaPlayer!!.setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())

            }

            mMediaPlayer!!.setDataSource(mSelectedSong!!.path)
            mMediaPlayer!!.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setCurrentSong(song: Song, songs: List<Song>) {
        mSelectedSong = song
        mSongs = songs
    }

    override fun getMediaPlayer(): MediaPlayer? {
        return mMediaPlayer
    }

    override fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }



}