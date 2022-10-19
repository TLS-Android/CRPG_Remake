package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.common.ui.model.Event
import java.util.*

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext

    private val _publicEventList = MutableLiveData<List<Event>?>()
    val publicEventList: LiveData<List<Event>?> = _publicEventList

    private val _privateEventList = MutableLiveData<List<Event>?>()
    val privateEventList: LiveData<List<Event>?> = _privateEventList

    private val _mDataList = MutableLiveData<List<Event>?>()
    val mDataList: LiveData<List<Event>?> = _mDataList

    private val _currentMonth = MutableLiveData<Int?>()
    val currentMonth: LiveData<Int?> = _currentMonth

    companion object { private val repo = AgendaRepository }

    private fun getDatesOfNextMonth(): List<Date> {
        _currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }


}
