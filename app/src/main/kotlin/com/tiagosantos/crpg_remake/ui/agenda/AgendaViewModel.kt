@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.tiagosantos.common.ui.extension.plusAssign
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils
import com.tiagosantos.common.ui.utils.Constants.SLASH
import com.tiagosantos.common.ui.utils.Constants.eventFilename
import com.tiagosantos.common.ui.utils.Constants.fileContent
import com.tiagosantos.crpg_remake.ui.agenda.AgendaRepository.mockActivityEvent
import com.tiagosantos.crpg_remake.ui.agenda.AgendaRepository.mockDinnerEvent
import com.tiagosantos.crpg_remake.ui.agenda.AgendaRepository.mockLunchEvent
import java.io.File
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
    private val _publicEventList = MutableLiveData<Event>()
    val publicEventList: LiveData<Event> = _publicEventList

    private val _privateEventList = MutableLiveData<Event>()
    val privateEventList: LiveData<Event> = _privateEventList

    /** MediatorLiveData allows us to merge multiple LiveData sources into one single LiveData which we then can observe.**/
    val liveDataList = MediatorLiveData<MutableList<Event>>()

    init {
        liveDataList.addSource(_publicEventList) { t -> liveDataList.plusAssign(t) }
        liveDataList.addSource(_privateEventList) { t -> liveDataList.plusAssign(t) }
    }

    private val _currentMonth = MutableLiveData<Int?>()
    val currentMonth: LiveData<Int?> = _currentMonth

    private val fullFilename = context?.filesDir.toString() + SLASH + eventFilename
    private val calendar = getInstance()

    companion object { private val repo = AgendaRepository }

    fun getFutureDatesOfCurrentMonth(): List<Date> {
        _currentMonth.value = calendar[MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        with(calendar){
            set(MONTH, currentMonth.value!!)
            set(DAY_OF_MONTH, 1)
            list.add(time)
            while (currentMonth.value == calendar[MONTH]) {
                add(DATE, +1)
                if (calendar[MONTH] == currentMonth.value) list.add(time)
            }
            add(DATE, -1)
        }

        return list
    }

    private val newFile: File = File(fullFilename)
    private fun populateFile() = newFile.apply { createNewFile(); writeText(fileContent) }

    fun getEventCollection() {
        populateFile()
        addMealsToPublicEvents()
        addMealsToPrivateEvents()
        addActivityEvent()
    }

    private fun addMealsToPublicEvents() { _publicEventList.value = mockLunchEvent }
    private fun addMealsToPrivateEvents() { _privateEventList.value = mockDinnerEvent }
    private fun addActivityEvent() { _privateEventList.value = mockActivityEvent }

    fun setSelectedDate(date: Date) {
        _selectedDate.value =
            DateUtils.getDayNumber(date) + DateUtils.getMonthNumber(date) + DateUtils.getYear(date)
    }

    private fun getDatesOfNextMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.plus(1) // + because we want next month
        if (currentMonth.value == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(YEAR, calendar[YEAR] + 1)
            _currentMonth.value = 0 // 0 == january
        }

        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        _currentMonth.value = currentMonth.value?.minus(1)
        if (currentMonth.value == 12) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(YEAR, calendar[YEAR] - 1)
            _currentMonth.value = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

}
