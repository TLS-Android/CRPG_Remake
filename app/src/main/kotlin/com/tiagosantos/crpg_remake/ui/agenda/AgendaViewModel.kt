package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.utils.Constants.FILES_DIR
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext

    private val _publicEventList = MutableLiveData<List<Event>?>()
    val publicEventList: LiveData<List<Event>?> = _publicEventList

    private val _privateEventList = MutableLiveData<List<Event>?>()
    val privateEventList: LiveData<List<Event>?> = _privateEventList

    private val _mDataList = MutableLiveData<List<Event>?>()
    val mDataList: LiveData<List<Event>?> = _mDataList

    companion object {
        private val repo = AgendaRepository
        val gson = Gson()
    }


}
