package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.common.ui.model.Event
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
    val mDataList: LiveData<List<Event>?> = _mDataList

    private val _currentMonth = MutableLiveData<Int?>()
    private val currentMonth: LiveData<Int?> = _currentMonth

    private var firstTimeFlag = false

    val calendar : Calendar
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

}
