package com.movemedical.recyclerview.sample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.movemedical.recyclerview.sample.model.MainModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val data = MutableLiveData<List<MainModel>>()

    fun buildDummyData() {
        val list = arrayListOf<MainModel>()

        for (i in 0..20) {
            val model = MainModel()
            model.name = "Name: $1"

            list.add(
                model
            )
        }
        data.postValue(list)
    }
}
