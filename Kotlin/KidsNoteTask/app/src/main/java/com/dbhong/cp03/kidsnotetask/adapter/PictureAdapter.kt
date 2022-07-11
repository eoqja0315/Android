package com.dbhong.cp03.kidsnotetask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ItemPictureBinding
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.dbhong.cp03.kidsnotetask.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PictureAdapter(
    private val itemClickListener: (LiveData<Picture>) -> Unit,
    private val likeImageButtonClickListener : (Picture) -> Unit,
) :

    ListAdapter<Picture, PictureAdapter.PictureAdapterViewHolder>(diffUtil) {
    private var pictures = emptyList<Picture>()

    inner class PictureAdapterViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pictureModel: Picture) {
            Log.i(TAG, "++ [I] : bind ++")
            binding.textViewAuthor.text = pictureModel.author
            binding.textViewImageSize.text = "${pictureModel.width} x ${pictureModel.height}"

            setImageButtonResource(pictureModel.like)

            Glide.with(binding.imageView.context)
                .load(pictureModel.downloadUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                val livePicture = MutableLiveData<Picture>()
                livePicture.value = pictureModel
                itemClickListener(livePicture)
                //itemClickListener(pictureModel)
            }


            binding.imageButtonLike.setOnClickListener {
                if(pictureModel.like.not()) {
                    pictureModel.like = true
                    likeImageButtonClickListener(pictureModel)
                } else {
                    pictureModel.like = false
                    likeImageButtonClickListener(pictureModel)
                }
            }
        }

        private fun setImageButtonResource(like : Boolean) {
            Log.i(TAG, "++ [I] : setImageButtonResource : $like ++")
            if(like) {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_cancel_24))
            } else {
                binding.imageButtonLike.setImageDrawable(getDrawable(binding.root.context, R.drawable.ic_baseline_check_circle_24))
            }
        }
    }

    internal fun setPictures(pictures: List<Picture>) {
        Log.i(TAG, "++ [I] : setPicture ++")
        this.pictures = pictures
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pictures.size ?: 0
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