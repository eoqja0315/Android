package com.dbhong.cp03.kidsnotetask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ItemPictureBinding
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PictureAdapter(private val itemClickListener: (Picture) -> Unit) :

    ListAdapter<Picture, PictureAdapter.PictureAdapterViewHolder>(diffUtil) {
    private var pictures = emptyList<Picture>()

    inner class PictureAdapterViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pictureModel: Picture) {
            Log.e(TAG, "++ bind ++")

            binding.textViewAuthor.text = pictureModel.author
            binding.textViewImageSize.text = "${pictureModel.width} x ${pictureModel.height}"

            Glide.with(binding.imageView.context)
                .load(pictureModel.downloadUrl)
                .into(binding.imageView)

            getLikeDataFromDB(getAppDatabase(binding.root.context), pictureModel){
                GlobalScope.launch(Dispatchers.Main) {
                    setImageButtonResource(it)
                }
            }

            binding.root.setOnClickListener {
                itemClickListener(pictureModel)
            }

            binding.imageButtonLike.setOnClickListener {
                if(pictureModel.like.not()) {
                    like(getAppDatabase(binding.root.context), pictureModel)
                } else {
                    dislike(getAppDatabase(binding.root.context), pictureModel)
                }
            }
        }

        private fun like(db : AppDatabase, picture : Picture) {
            setLikeDataToDB(db, picture, true)
        }

        private fun dislike(db : AppDatabase, picture : Picture) {
            setLikeDataToDB(db, picture, false)
        }

        private fun setLikeDataToDB(db : AppDatabase, picture: Picture, like : Boolean) {
            GlobalScope.launch {
                picture.like = like
                db.pictureDao().insertPicture(picture)
                GlobalScope.launch(Dispatchers.Main) {
                    setImageButtonResource(like)
                }
            }
        }

        private fun getLikeDataFromDB(db : AppDatabase, picture: Picture, listener : (Boolean) -> (Unit)) {
            GlobalScope.launch {
                picture.like = db.pictureDao().getOnePictureById(picture.id)?.like ?: return@launch
                listener(picture.like)
            }
        }

        private fun setImageButtonResource(like : Boolean) {
            if(like) {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_flag_24))
            } else {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_outlined_flag_24))
            }
        }
    }

    internal fun setPictures(pictures : List<Picture>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapterViewHolder {
        return PictureAdapterViewHolder(
            ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PictureAdapterViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    companion object {
        const val TAG = "PictureAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<Picture>() {
            override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}