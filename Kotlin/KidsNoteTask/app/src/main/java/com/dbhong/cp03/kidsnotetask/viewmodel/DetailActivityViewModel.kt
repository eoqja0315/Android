package com.dbhong.cp03.kidsnotetask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.dbhong.cp03.kidsnotetask.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var id : Int = -1
    private val database = getAppDatabase(application.applicationContext)
    private var repository = DetailRepository(database, id)

    var detailPicture = repository.detailPicsumPicture

    fun insertPicture(picsumPicture: PicsumPicture) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPicture(picsumPicture)
    }

    fun setId(id : Int) {
        this.id = id
        setPicture()
    }

    fun getId() : Int {
        return id
    }

    fun updateLike(picture : PicsumPicture) {
        val pictures = repository.allPicsumPicture.value

        if(pictures != null) {
            for(i in pictures.indices){
                if(pictures[i].id == picture.id){
                    pictures[i].like = picture.like
                    break
                }
            }
        }
    }

    private fun setPicture() {
        repository = DetailRepository(database, this.id)
        detailPicture = repository.detailPicsumPicture
    }
}