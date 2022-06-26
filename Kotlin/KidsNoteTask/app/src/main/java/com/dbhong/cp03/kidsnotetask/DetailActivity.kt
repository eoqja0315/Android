package com.dbhong.cp03.kidsnotetask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.databinding.ActivityDetailBinding
import com.dbhong.cp03.kidsnotetask.model.Picture

class DetailActivity : AppCompatActivity() {

    private lateinit var db : AppDatabase
    private lateinit var binding : ActivityDetailBinding
    private lateinit var picture : Picture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pictureModel = intent.getParcelableExtra<Picture>("pictureModel")

        if(pictureModel != null) {
            picture = pictureModel
        }

        db = getAppDatabase(this)

        if(picture == null) {
            Toast.makeText(this, "Failed to get picture data", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            initViews()
            bindingViews()
        }
    }

    private fun initViews() {
        binding.textViewAuthor.text = picture.author
        binding.textViewImageSize.text = "${picture.width} x ${picture.height}"

        Glide.with(binding.imageView.context)
            .load(picture.downloadUrl)
            .into(binding.imageView)

        if(picture.like) {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_flag_24))
        }
    }

    private fun bindingViews() {
        binding.imageButtonLike.setOnClickListener {
            if(picture.like.not()) {
                like()
            }
            else {
                dislike()
            }
        }
    }

    private fun like() {
        Thread {
            db.pictureDao().savePictureById(
                Picture(
                    id = picture.id,
                    author = picture.author,
                    width = picture.width,
                    height = picture.height,
                    url = picture.url,
                    downloadUrl = picture.downloadUrl,
                    true
                )
            )
        }.start()

        picture.like = true
        binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_flag_24))
    }

    private fun dislike() {
        Thread {
            db.pictureDao().savePictureById(
                Picture(
                    id = picture.id,
                    author = picture.author,
                    width = picture.width,
                    height = picture.height,
                    url = picture.url,
                    downloadUrl = picture.downloadUrl,
                    false
                )
            )
        }.start()

        picture.like = false
        binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_outlined_flag_24))
    }

    companion object {
        const val TAG = "DetailActivity"
    }
}