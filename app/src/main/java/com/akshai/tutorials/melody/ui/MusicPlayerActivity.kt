package com.akshai.tutorials.melody.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.akshai.tutorials.melody.R
import com.akshai.tutorials.melody.adaptor.MusicAdaptor
import com.akshai.tutorials.melody.adaptor.PlayerAdaptor
import com.akshai.tutorials.melody.databinding.ActivityMusicPlayerBinding
import com.akshai.tutorials.melody.model.Song
import com.akshai.tutorials.melody.service.MusicService
import com.akshai.tutorials.melody.utils.MusicProvider
import com.google.android.material.snackbar.Snackbar

class MusicPlayerActivity : AppCompatActivity() {

    private var isBind: Boolean = false
    private lateinit var binding: ActivityMusicPlayerBinding
    private var deviceMusic = mutableListOf<Song>()

    private var mMusicService: MusicService? = null
    private var mPlayerAdapter: PlayerAdaptor? = null

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMusicService = (service as MusicService.LocalBinder).instance
            mPlayerAdapter = mMusicService!!.mediaPlayerHolder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mMusicService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            mPlayerAdapter!!.getMediaPlayer()!!.also {
                if (it.isPlaying){
                    it.pause()
                }else{
                    it.start()
                }
            }
        }

        doBindService()
        checkReadStoragePermissions()

    }

    private fun checkReadStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }else getMusic()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }else getMusic()
    }

    private fun getMusic() {
        deviceMusic.addAll(MusicProvider.getAllDeviceSongs(this))
        binding.content.recyclerView.apply {
            adapter = MusicAdaptor(
                onSongClick = {
                    onSongSelected(it,deviceMusic)
                }
            ).also {
                it.setSongs(deviceMusic)
            }
        }
    }

    private fun onSongSelected(song: Song, songs: MutableList<Song>) {
        try {
            mPlayerAdapter!!.setCurrentSong(song, songs)
            mPlayerAdapter!!.initMediaPlayer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doBindService() {
        bindService(Intent(this, MusicService::class.java),
            mConnection, BIND_AUTO_CREATE)
        isBind = true
        startService(Intent(this, MusicService::class.java))
    }


    private fun doUnBindService() {
        if (isBind) {
            stopService(Intent(this, MusicService::class.java))
            unbindService(mConnection)
            isBind = false
        }
    }

}