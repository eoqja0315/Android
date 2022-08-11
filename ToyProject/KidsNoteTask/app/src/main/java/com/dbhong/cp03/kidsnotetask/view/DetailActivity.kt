package com.dbhong.cp03.kidsnotetask.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ActivityDetailBinding
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.dbhong.cp03.kidsnotetask.viewmodel.DetailActivityViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var picsumPicture: PicsumPicture
    private val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        initViews()
        bindingViews()

        viewModel.setId(picsumPicture.id)
        viewModel.insertPicture(picsumPicture)
        viewModel.detailPicture.observe(this, Observer {
            it?.let {
                viewModel.updateLike(it)
                setImageButtonResource(it.like)
            }
        })
    }

    private fun initViews() {
        binding.textViewAuthor.text = picsumPicture.author
        binding.textViewImageSize.text = "${picsumPicture.width} x ${picsumPicture.height}"

        Glide.with(binding.imageView.context)
            .load(picsumPicture.downloadUrl)
            .into(binding.imageView)

        setImageButtonResource(picsumPicture.like)
    }

    private fun bindingViews() {
        binding.imageButtonLike.setOnClickListener {
            if (!picsumPicture.like) {
                picsumPicture.like = true
                viewModel.insertPicture(picsumPicture)
            } else {
                picsumPicture.like = false
                viewModel.insertPicture(picsumPicture)
            }
        }
    }

    private fun getData() {
        picsumPicture = PicsumPicture(
            id = intent.getIntExtra(PICTURE_ID, -1),
            author = intent.getStringExtra(PICTURE_AUTHOR) ?: "",
            width = intent.getIntExtra(PICTURE_WIDTH, -1),
            height = intent.getIntExtra(PICTURE_HEIGHT, -1),
            url = intent.getStringExtra(PICTURE_URL) ?: "",
            downloadUrl = intent.getStringExtra(PICTURE_DOWNLOAD_URL) ?: "",
            like = intent.getBooleanExtra(PICTURE_LIKE, false)
        )
    }

    private fun setImageButtonResource(like: Boolean) {
        if (like) {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_circle_24))
        } else {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_cancel_24))
        }
    }

    companion object {
        const val PICTURE_MODEL_INTENT_NAME = "pictureModel"
        const val TAG = "DetailActivity"

        const val PICTURE_ID = "id"
        const val PICTURE_AUTHOR = "author"
        const val PICTURE_WIDTH = "width"
        const val PICTURE_HEIGHT = "height"
        const val PICTURE_URL = "url"
        const val PICTURE_DOWNLOAD_URL = "downloadUrl"
        const val PICTURE_LIKE = "like"
    }
}