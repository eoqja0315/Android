package com.dbhong.cp03.ch04

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.dbhong.cp03.ch04.databinding.ActivityDetailBinding
import com.dbhong.cp03.ch04.model.Book
import com.dbhong.cp03.ch04.model.Review

class DetailActivity : AppCompatActivity(){

    private lateinit var db : AppDatabase
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review = db.reviewDao().getOneReview(model?.isbn ?: "")
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.isbn ?: "",
                    binding.reviewEditText.text.toString()
                        )
                )
            }.start()
        }
    }

}