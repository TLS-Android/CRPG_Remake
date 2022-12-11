@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

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
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils
import com.tiagosantos.common.ui.utils.Constants.SLASH
import com.tiagosantos.common.ui.utils.Constants.eventFilename
import com.tiagosantos.common.ui.utils.Constants.fileContent
import com.tiagosantos.crpg_remake.ui.agenda.AgendaRepository.dinnerEvent
import com.tiagosantos.crpg_remake.ui.agenda.AgendaRepository.lunchEvent
import com.tiagosantos.crpg_remake.ui.meals.MealsViewModel.Companion.gson
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*
import java.util.Calendar.*

/**
 *
 * To summarize, the key difference would be:
 * setValue() method must be called from the main thread.
 * But if you need set a value from a background thread, postValue() should be used.
 */
class AgendaViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext

    private val _selectedDate = MutableLiveData<String?>()
    val selectedDate: LiveData<String?> = _selectedDate

    /** MutableLiveData should always be val; only the contents can be updated **/
    private val _publicEventList = MutableLiveData<MutableList<Event>?>()
    val publicEventList: LiveData<MutableList<Event>?> = _publicEventList

    private val _privateEventList = MutableLiveData<MutableList<Event>?>()
    val privateEventList: LiveData<MutableList<Event>?> = _privateEventList

    private val _mDataList = MutableLiveData<MutableList<Event>?>()
    var mDataList: LiveData<MutableList<Event>?> = _mDataList

    private val _currentMonth = MutableLiveData<Int?>()
    val currentMonth: LiveData<Int?> = _currentMonth

    private val fullFilename = context?.filesDir.toString() + SLASH + eventFilename

    private val calendar : Calendar
        get() {
            TODO()
        }

    companion object { private val repo = AgendaRepository }

    private fun getDatesOfNextMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.plus(1) // + because we want next month
        if (currentMonth.value == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(YEAR, calendar[YEAR] + 1)
            _currentMonth.value = 0 // 0 == january
        }

        return getDates(mutableListOf())
    }

    fun getDatesOfPreviousMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.minus(1)
        if (currentMonth.value == 12) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(YEAR, calendar[YEAR] - 1)
            _currentMonth.value = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    fun getFutureDatesOfCurrentMonth(): List<Date> {
        _currentMonth.value = calendar[MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(MONTH, currentMonth.value!!)
        calendar.set(DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth.value == calendar[MONTH]) {
            calendar.add(DATE, +1)
            if (calendar[MONTH] == currentMonth.value)
                list.add(calendar.time)
        }
        calendar.add(DATE, -1)
        return list
    }

    private val newFile: File = File(fullFilename)
    private fun populateFile() = newFile.apply { createNewFile(); writeText(fileContent) }

    val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
    private val _privateList: MutableLiveData<MutableList<Event>?> =
        gson.fromJson(FileReader(newFile.absoluteFile), type)

    fun getEventCollectionFromJSON() {
        populateFile()
        concatenatePublicPrivateEvents()
        _privateList.value!!.sortBy { it.start_time }
    }

    private fun concatenatePublicPrivateEvents(): LiveData<MutableList<Event>?> {
        addMealsToPrivateEvents()
        addMealsToPublicEvents()
        _mDataList.value?.plusAssign(privateEventList.value!! + publicEventList.value!!)
        return _mDataList
    }

    private fun addMealsToPrivateEvents(): LiveData<MutableList<Event>?> {
        _privateEventList.value?.apply { add(lunchEvent); add(dinnerEvent) }
        return privateEventList
    }

    private fun addMealsToPublicEvents(): LiveData<MutableList<Event>?> {
        _publicEventList.value?.apply { add(lunchEvent); add(dinnerEvent) }
        return publicEventList
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value =
            DateUtils.getDayNumber(date) + DateUtils.getMonthNumber(date) + DateUtils.getYear(date)
    }

}
