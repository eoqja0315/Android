package com.dbhong.cp03.kidsnotetask.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dbhong.cp03.kidsnotetask.`interface`.NetworkCallback
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.dbhong.cp03.kidsnotetask.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application){

    private val database = getAppDatabase(application.applicationContext)
    private val repository = Repository(database)
    var allPicture = repository.allPicsumPicture
    var localAllPicture = repository.localAllPicsumPicture

    fun insertPicsumPicture(picsumPicture: PicsumPicture) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPicture(picsumPicture)
    }

    @ExperimentalCoroutinesApi
    fun getResultOfPicsumPicturesFromServer() : Flow<String> = callbackFlow{
        val callbackImpl = object : NetworkCallback {
            override fun success(resultCode : Int) {
                if(resultCode == NetworkCallback.SUCCESS) {
                    Log.i(TAG, "++ [i] SUCCESS ${allPicture.value?.get(0)?.like} ++")
                    updateLike()
                    trySend("SUCCESS")
                }
            }

            override fun fail() {
                trySend("FAILED")
            }
        }

        getPicsumPicturesFromServer(callbackImpl)

        //coroutine scope가 cancel 또는 close 될때 호출
        awaitClose {

        }
    }

    fun getAll() : List<PicsumPicture>{
        return allPicture.value ?: emptyList()
    }

    fun updateLike() {
        val localAllPicture = this.localAllPicture.value ?: return
        val allPicture = this.allPicture.value ?: return

        for (i in allPicture.indices) {
            val picture = allPicture[i]

            for (j in localAllPicture.indices) {
                val localPicture = localAllPicture[j]

                if (picture.id == localPicture.id) {
                    picture.like = localPicture.like
                    break
                }
            }
        }
    }

    fun updateLike(localAllPicsumPicture: List<PicsumPicture>) : List<PicsumPicture>{
        val allPicture = this.allPicture.value ?: return emptyList()

        for(i in allPicture.indices){
            val picture = allPicture[i]

            for(j in localAllPicsumPicture.indices) {
                val localPicture = localAllPicsumPicture[j]

                if(picture.id == localPicture.id){
                    picture.like = localPicture.like
                    break
                }
            }
        }

        return allPicture
    }

    private fun getPicsumPicturesFromServer(networkCallback : NetworkCallback) = viewModelScope.launch(Dispatchers.IO) {
        repository.getPicsumPicturesFromServer(networkCallback)
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}