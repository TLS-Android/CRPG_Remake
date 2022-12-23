package com.tiagosantos.crpg_remake.ui.agenda

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.hide
import com.tiagosantos.common.ui.extension.show
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentDatePickerBinding
import com.tiagosantos.common.ui.singlerowcalendar.calendar.CalendarChangesObserver
import com.tiagosantos.common.ui.singlerowcalendar.calendar.CalendarViewManager
import com.tiagosantos.common.ui.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.tiagosantos.common.ui.singlerowcalendar.selection.CalendarSelectionManager
import com.tiagosantos.common.ui.singlerowcalendar.calendar.SingleRowCalendar
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.buildDateString
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDay3LettersName
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDayName
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDayNumber
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import java.util.*
import java.util.Calendar.*

class DatePickerFragment: BaseModalFragment<FragmentDatePickerBinding>(
    layoutId = R.layout.fragment_date_picker,
    FragmentSettings(
        appBarTitle = "ESCOLHER DATA",
        sharedPreferencesBooleanName = R.string.datePickerHasRun.toString(),
    ),
    ttsSettings = TTSSettings(
        "Selecione um dia premindo item que pretender"
    ),
    srSettings = SRSettings(
        isListening = false,
    )
) {
    private var selected = false
    private val calendar = getInstance()

    val vm: AgendaViewModel by viewModels()

    companion object{
        val map = mapOf(
            "um" to 1, "dois" to 2, "três" to 3, "quatro" to 4,
            "cinco" to 5, "seis" to 6, "sete" to 7, "oito" to 8, "nove" to 9, "dez" to 10
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewB) {
            calendar.time = Date()
            val myCalendarViewManager = object : CalendarViewManager {
                override fun setCalendarViewResourceId(
                    position: Int,
                    date: Date,
                    isSelected: Boolean,
                ): Int {
                    val cal = getInstance().apply { time = date }
                    if (!isSelected) tvDate.text = getString(R.string.nenhum_dia_selecionado_msg)

                    return if (isSelected)
                        when (cal[DAY_OF_WEEK]) {
                            else -> R.layout.selected_calendar_item
                        }
                    else
                        when (cal[DAY_OF_WEEK]) {
                            else -> R.layout.calendar_item
                        }
                }

                override fun bindDataToCalendarView(
                    holder: SingleRowCalendarAdapter.CalendarViewHolder,
                    date: Date,
                    position: Int,
                    isSelected: Boolean,
                ) = with(viewB.calendarItem) {
                    tvDateCalendarItem.text = getDayNumber(date)
                    tvDayCalendarItem.text = getDay3LettersName(date)
                }
            }

            val myCalendarChangesObserver = object :
                CalendarChangesObserver {
                override fun whenSelectionChanged(
                    isSelected: Boolean,
                    position: Int,
                    date: Date
                ) {
                    super.whenSelectionChanged(isSelected, position, date)

                    tvDate.text = buildDateString(date)
                    tvDay.text = getDayName(date)
                    vm.setSelectedDate(date)
                    selected = isSelected
                }
            }

            val mySelectionManager = object : CalendarSelectionManager {
                override fun canBeItemSelected(position: Int, date: Date): Boolean {
                    val cal = getInstance().apply { time = date }
                    return when (cal[DAY_OF_WEEK]) {
                        SATURDAY, SUNDAY -> false
                        else -> true
                    }
                }
            }

            mainSingleRowCalendar.apply {
                calendarViewManager = myCalendarViewManager
                calendarChangesObserver = myCalendarChangesObserver
                calendarSelectionManager = mySelectionManager
                setDates(vm.getFutureDatesOfCurrentMonth())
                init()
            }

            println("View manager: " + mainSingleRowCalendar.calendarViewManager.toString())
            println("dates ${mainSingleRowCalendar.getDates()}")

            buttonSelecionar.setOnClickListener {
                if (selected) { noDateSelectedWarning.hide()
                    goToFragment(AgendaFragment())
                } else {
                    noDateSelectedWarning.show()
                }
            }
        }
    }

    private fun performActionWithVoiceCommand(
        command: String,
        singleRowCalendar: SingleRowCalendar
    ) {
        val literalValue = map.getOrElse(command) {
            println("Número não presente na lista")
        } as Int

        singleRowCalendar.let {
            it.clearSelection()
            if (literalValue > 5) it.scrollToPosition(literalValue - 1)
            it.select(literalValue - 1)
        }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }
}
