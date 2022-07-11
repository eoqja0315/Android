package com.dbhong.cp03.kidsnotetask.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.databinding.ActivityDetailBinding
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.dbhong.cp03.kidsnotetask.viewmodel.DetailActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityDetailBinding
    private lateinit var picture: MutableLiveData<Picture>

    private val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = getAppDatabase(this)

        //val pictureModel = intent.getParcelableExtra<Picture>(PICTURE_MODEL_INTENT_NAME)

        val pictureModel = Picture(
            id = intent.getIntExtra(PICTURE_ID, -1),
            author = intent.getStringExtra(PICTURE_AUTHOR) ?: "",
            width = intent.getIntExtra(PICTURE_WIDTH, -1),
            height = intent.getIntExtra(PICTURE_HEIGHT, -1),
            url = intent.getStringExtra(PICTURE_URL) ?: "",
            downloadUrl = intent.getStringExtra(PICTURE_DOWNLOAD_URL) ?: "",
            like = intent.getBooleanExtra(PICTURE_LIKE, false)
        )

        viewModel.detailPicture.observe(this, Observer {
            it?.let {
                setImageButtonResource(it.like)
            }
        })

        picture.value = pictureModel
        initViews()
        bindingViews()
    }

    private fun initViews() {
        binding.textViewAuthor.text = picture.value?.author
        binding.textViewImageSize.text = "${picture.value?.width} x ${picture.value?.height}"

        Glide.with(binding.imageView.context)
            .load(picture.value?.downloadUrl)
            .into(binding.imageView)

        setImageButtonResource(picture.value?.like ?: false)
    }

    private fun bindingViews() {
        binding.imageButtonLike.setOnClickListener {
            if(picture.value != null) {
                if (picture.value?.like == false) {
                    picture.value?.like = true
                    viewModel.insertPicture(picture.value!!)
                    //like()
                } else {
                    picture.value?.like = false
                    viewModel.insertPicture(picture.value!!)
                    //dislike()
                }
            }
        }
    }

//    private fun like() {
//        setLikeDataToDB(true){
//            GlobalScope.launch(Dispatchers.Main) {
//                setImageButtonResource(it)
//            }
//        }
//    }
//
//    private fun dislike() {
//        setLikeDataToDB(false){
//            GlobalScope.launch(Dispatchers.Main) {
//                setImageButtonResource(it)
//            }
//        }
//    }
//
//    private fun setLikeDataToDB(like : Boolean, listener : (Boolean?) -> (Unit)) {
//        GlobalScope.launch {
//            picture.value?.like = like
//            db.pictureDao().insertPicture(picture)
//            listener(picture.value?.like)
//        }
//    }

    private fun setImageButtonResource(like: Boolean) {
        if (like) {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_cancel_24))
        } else {
            binding.imageButtonLike.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_circle_24))
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