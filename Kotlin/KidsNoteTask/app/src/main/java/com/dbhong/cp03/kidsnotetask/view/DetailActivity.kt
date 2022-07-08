package com.dbhong.cp03.kidsnotetask.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ActivityDetailBinding
import com.dbhong.cp03.kidsnotetask.getAppDatabase
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
        val pictureModel = intent.getParcelableExtra<Picture>(PICTURE_MODEL_INTENT_NAME)

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

        setImageButtonResource(picture.like)
    }

    private fun bindingViews() {
        binding.imageButtonLike.setOnClickListener {
            if (!picture.like) {
                like()
            } else {
                dislike()
            }
        }
    }

    private fun like() {
        setLikeDataToDB(true){
            GlobalScope.launch(Dispatchers.Main) {
                setImageButtonResource(it)
            }
        }
    }

    private fun dislike() {
        setLikeDataToDB(false){
            GlobalScope.launch(Dispatchers.Main) {
                setImageButtonResource(it)
            }
        }
    }

    private fun setLikeDataToDB(like : Boolean, listener : (Boolean) -> (Unit)) {
        GlobalScope.launch {
            picture.like = like
            db.pictureDao().insertPicture(picture)
            listener(picture.like)
        }
    }

    private fun setImageButtonResource(like : Boolean) {
        if(like) {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_flag_24))
        } else {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_outlined_flag_24))
        }
    }

    companion object {
        const val PICTURE_MODEL_INTENT_NAME = "pictureModel"
        const val TAG = "DetailActivity"
    }
}