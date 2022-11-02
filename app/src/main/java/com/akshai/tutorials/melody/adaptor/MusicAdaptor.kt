package com.akshai.tutorials.melody.adaptor

import android.R.attr.data
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akshai.tutorials.melody.databinding.LayoytSongItemBinding
import com.akshai.tutorials.melody.model.Song


/**
 * Created by ATM on 07/October/2022
 */

class MusicAdaptor(
    private val onSongClick: (song: Song) -> Unit,
) : RecyclerView.Adapter<MusicAdaptor.SongHolder>() {

    private var songList: List<Song> = arrayListOf()

    fun setSongs(songs: List<Song>) {
        songList = songs
        notifyDataSetChanged()
    }


    inner class SongHolder(private val binding: LayoytSongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(song: Song, position: Int) {
            binding.apply {
                textView.text = song.title
                imageView.load(song.albumArt)
               // imageView.setImageBitmap(getAlbumArtBitMap(binding.root.context, song.albumArt))

                item.setOnClickListener { onSongClick.invoke(song) }

            }
        }

    }

    private fun getAlbumArtBitMap(context: Context, albumArt: Uri): Bitmap {
        var bitmap : Bitmap? = null
        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, albumArt)
        return bitmap
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return SongHolder(
            LayoytSongItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.setData(songList[position], position)
    }

    override fun getItemCount(): Int =
        songList.size

}