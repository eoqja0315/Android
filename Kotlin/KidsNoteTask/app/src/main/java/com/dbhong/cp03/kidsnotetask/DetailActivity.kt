package com.dbhong.cp03.kidsnotetask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.databinding.ActivityDetailBinding
import com.dbhong.cp03.kidsnotetask.model.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityDetailBinding
    private lateinit var picture: Picture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = getAppDatabase(this)
        val pictureModel = intent.getParcelableExtra<Picture>("pictureModel")

        if (pictureModel != null) {
            picture = pictureModel
            initViews()
            bindingViews()
        } else {
            Toast.makeText(this, "Failed to get picture data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initViews() {
        binding.textViewAuthor.text = picture.author
        binding.textViewImageSize.text = "${picture.width} x ${picture.height}"

        Glide.with(binding.imageView.context)
            .load(picture.downloadUrl)
            .into(binding.imageView)

        if (picture.like) {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_flag_24))
        }
    }

    private fun bindingViews() {
        binding.imageButtonLike.setOnClickListener {
            if (picture.like.not()) {
                like {
                    binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_flag_24))
                }
            } else {
                dislike {
                    binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_outlined_flag_24))
                }
            }
        }
    }

    private fun like(listener : () -> (Unit)) {
        GlobalScope.launch {
            picture.like = true
            db.pictureDao().savePictureById(
                picture
            )
            GlobalScope.launch(Dispatchers.Main) {
                listener()
            }
        }
    }

    private fun dislike(listener: () -> Unit) {
        GlobalScope.launch {
            picture.like = false
            db.pictureDao().savePictureById(picture)
            GlobalScope.launch(Dispatchers.Main) {
                listener()
            }
        }
    }

//    private fun like(listener: () -> (Unit)) {
//        Thread {
//            picture.like = true
//            db.pictureDao().savePictureById(picture)
//            listener()
//        }.start()
//    }
//
//    private fun dislike(listener: () -> (Unit)) {
//        Thread {
//            picture.like = false
//            db.pictureDao().savePictureById(picture)
//        }.start()
//        listener()
//    }

    companion object {
        const val TAG = "DetailActivity"
    }
}