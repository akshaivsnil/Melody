package com.akshai.tutorials.melody.adaptor

import android.media.MediaPlayer
import com.akshai.tutorials.melody.model.Song


/**
 * Created by ATM on 07/October/2022
 */

interface PlayerAdaptor {

    fun initMediaPlayer()

    fun release()

    fun setCurrentSong(song: Song, songs: List<Song>)

    fun getMediaPlayer(): MediaPlayer?

}