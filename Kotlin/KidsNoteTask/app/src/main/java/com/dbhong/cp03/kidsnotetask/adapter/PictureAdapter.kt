package com.dbhong.cp03.kidsnotetask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ItemPictureBinding
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.dbhong.cp03.kidsnotetask.view.MainActivity

class PictureAdapter(
    private val itemClickListener: (LiveData<PicsumPicture>) -> Unit,
    private val likeImageButtonClickListener : (PicsumPicture) -> Unit,
) :

    ListAdapter<PicsumPicture, PictureAdapter.PictureAdapterViewHolder>(diffUtil) {
    private var picsumPictures = emptyList<PicsumPicture>()

    inner class PictureAdapterViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(picsumPictureModel: PicsumPicture) {
            Log.i(TAG, "++ [I] : bind ++")
            binding.textViewAuthor.text = picsumPictureModel.author
            binding.textViewImageSize.text = "${picsumPictureModel.width} x ${picsumPictureModel.height}"

            setImageButtonResource(picsumPictureModel.like)

            Glide.with(binding.imageView.context)
                .load(picsumPictureModel.downloadUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                Log.i(TAG, "++ [I] : setOnClickListener ++")
                val livePicsumPicture = MutableLiveData<PicsumPicture>()
                livePicsumPicture.value = picsumPictureModel
                itemClickListener(livePicsumPicture)
            }

            binding.imageButtonLike.setOnClickListener {
                if(picsumPictureModel.like.not()) {
                    picsumPictureModel.like = true
                    likeImageButtonClickListener(picsumPictureModel)
                } else {
                    picsumPictureModel.like = false
                    likeImageButtonClickListener(picsumPictureModel)
                }
            }
        }

        private fun setImageButtonResource(like : Boolean) {
            Log.i(TAG, "++ [I] : setImageButtonResource : $like ++")
            if(like) {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_check_circle_24))
            } else {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_cancel_24))
            }
        }
    }

    internal fun setPictures(picsumPictures: List<PicsumPicture>) {
        Log.i(TAG, "++ [I] : setPicture ++")
        this.picsumPictures = picsumPictures
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return picsumPictures.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapterViewHolder {
        return PictureAdapterViewHolder(
            ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PictureAdapterViewHolder, position: Int) {

        holder.bind(picsumPictures[position])
    }

    companion object {
        const val TAG = "PictureAdapter"
        val diffUtil = object : DiffUtil.ItemCallback<PicsumPicture>() {
            override fun areItemsTheSame(oldItem: PicsumPicture, newItem: PicsumPicture): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PicsumPicture, newItem: PicsumPicture): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}