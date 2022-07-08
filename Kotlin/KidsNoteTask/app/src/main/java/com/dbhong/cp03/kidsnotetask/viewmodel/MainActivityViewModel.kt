package com.dbhong.cp03.kidsnotetask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.dbhong.cp03.kidsnotetask.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application){

    private val database = getAppDatabase(application.applicationContext)
    private val repository = Repository(database)
    var allPicture = repository.allPicture
    var localAllPicture = repository.localAllPicture

    fun getPicutresFromServer() = viewModelScope.launch(Dispatchers.IO) {
        repository.getPicturesFromServer()
        updateLikeData()
    }

    fun insertPicture(picture: Picture) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPicture(picture)
    }

    fun getAllPicture() : LiveData<List<Picture>> {
        return allPicture
    }

    private fun updateLikeData() {
        val localPictureList = localAllPicture.value
        val allPicture = this.allPicture.value

        if(localPictureList == null) {
            return
        }

        if(allPicture == null) {
            return
        }

        for(i in allPicture.indices){
            val picture = allPicture[i]

            for(j in localPictureList.indices) {
                val localPicture = localPictureList[j]

                if(picture.id == localPicture.id){
                    picture.like = localPicture.like
                }
            }
        }
    }
}