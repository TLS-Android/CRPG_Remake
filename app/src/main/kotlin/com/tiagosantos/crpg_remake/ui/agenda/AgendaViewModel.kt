package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.model.Event
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

class AgendaViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext

    private var _publicEventList = MutableLiveData<List<Event>?>()
    val publicEventList: LiveData<List<Event>?> = _publicEventList

    private var _privateEventList = MutableLiveData<List<Event>?>()
    val privateEventList: LiveData<List<Event>?> = _privateEventList

    private var _mDataList = MutableLiveData<List<Event>?>()
    var mDataList: LiveData<List<Event>?> = _mDataList

    private val _currentMonth = MutableLiveData<Int?>()
    private val currentMonth: LiveData<Int?> = _currentMonth

    private val calendar : Calendar
        get() {
            TODO()
        }

    companion object { private val repo = AgendaRepository }

    private fun getDatesOfNextMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.plus(1) // + because we want next month
        if (currentMonth.value == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            _currentMonth.value = 0 // 0 == january
        }

        return getDates(mutableListOf())
    }

    fun getDatesOfPreviousMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.minus(1) // - because we want previous
        // month
        if (currentMonth.value == 12) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            _currentMonth.value = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        _currentMonth.value = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth.value!!)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth.value == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth.value)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun newFile(): File {
        val filename = "event.json"
        val fullFilename = context?.filesDir.toString() + "/" + filename
        return File(fullFilename)
    }

    fun getEventCollectionFromJSON(): ArrayList<Event> {
        val file = new()
        populateFile()

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val privateList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        concatenatePublicPrivateEvents()
        return privateList
    }

    private fun concatenatePublicPrivateEvents(): ArrayList<Event> {
        addMealsToPrivateEvents()
        addMealsToPublicEvents()
        mDataList.plusAssign((privateEventList + publicEventList) as ArrayList<Event>)
        println("Size mDataList: " + mDataList.size)
        return mDataList
    }

    private fun populateFile() {
        // create a new file
        val file = newFile()
        val isNewFileCreated : Boolean = file.createNewFile()

        val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", 
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", "start_time": "2000","end_time": "2100","date": "2021-03-17"},{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", "start_time": "0830","end_time": "1330","date": "2021-03-17"},{"title": "Actividade","info":"Sala 12","type": "ACTIVITY", "start_time": "0830","end_time": "1330","date": "2021-03-17"}]""".trimMargin()

        File(fullFilename).writeText(fileContent)
    }

}
