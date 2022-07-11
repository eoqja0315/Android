package com.dbhong.cp03.kidsnotetask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dbhong.cp03.kidsnotetask.getAppDatabase
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.dbhong.cp03.kidsnotetask.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivityViewModel(application: Application, private val id : Int) : AndroidViewModel(application) {

    private val database = getAppDatabase(application.applicationContext)
    private val repository = DetailRepository(database, id)

    var detailPicture = repository.detailPicture

    fun insertPicture(picture: Picture) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPicture(picture)
    }
}