package com.dbhong.cp03.kidsnotetask.adapter

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

class PictureAdapter(private val itemClickListener: (Picture) -> Unit) :
    ListAdapter<Picture, PictureAdapter.PictureAdapterViewHolder>(diffUtil) {

    inner class PictureAdapterViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pictureModel: Picture) {
            binding.textViewAuthor.text = pictureModel.author
            binding.textViewImageSize.text = "${pictureModel.width} x ${pictureModel.height}"

            Glide.with(binding.imageView.context)
                .load(pictureModel.downloadUrl)
                .into(binding.imageView)

            getLikeDataFromDB(getAppDatabase(binding.root.context), pictureModel.id){
                if(it) {
                    binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_flag_24))
                }
            }

            binding.root.setOnClickListener {
                itemClickListener(pictureModel)
            }

            binding.imageButtonLike.setOnClickListener {
                if(pictureModel.like.not()) {
                    like(getAppDatabase(binding.root.context), pictureModel){
                        binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_flag_24))
                    }
                } else {
                    dislike(getAppDatabase(binding.root.context), pictureModel){
                        binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_outlined_flag_24))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapterViewHolder {
        return PictureAdapterViewHolder(
            ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PictureAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    private fun like(db : AppDatabase, picture : Picture, listener : () -> (Unit)) {
        Thread {
            picture.like = true
            db.pictureDao().savePictureById(
                picture
            )
            listener()
        }.start()
    }

    private fun dislike(db : AppDatabase, picture : Picture, listener: () -> Unit) {
        Thread {
            picture.like = false
            db.pictureDao().savePictureById(picture)
            listener()
        }.start()
    }

    private fun getLikeDataFromDB(db : AppDatabase, id: Int, listener: (Boolean) -> Unit) {
        Thread {
            val picture = db.pictureDao().getOnePictureById(id)
            listener(picture.like)
        }.start()
    }

    companion object {
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