package com.tiagosantos.crpg_remake.ui.agenda

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
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
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.buildDateString
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDay3LettersName
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDayName
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils.getDayNumber
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.numberMap
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.actionMap
import com.tiagosantos.crpg_remake.ui.meals.MealsFragment
import java.util.*
import java.util.Calendar.*

@FragmentWithArgs
class DatePickerFragment: BaseModalFragment<FragmentDatePickerBinding>() {

    /**
     * Lazy properties: the value is computed only on first access.
     */
    @delegate:Arg
    override val settings by lazy {
        FragmentSettings(
            appBarTitle = getString(R.string.escolher_data),
            sharedPreferencesBooleanName = R.string.datePickerHasRun.toString(),
            showBackButton = false,
            hideBottomNavigationView = true,
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(R.string.contextual_date_picker)
    }

    @delegate:Arg
    override val srSettings by lazy {
        SRSettings(
            commandList = feature.actionMap,
            isListening = false,
        )
    }

    val viewModel: AgendaViewModel by viewModels()

    private var selected = false
    private val calendar = getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        goToFragment(MealsFragment())
        //goToFragment(MeditationFragment())
        //goToFragment(ReminderFragment())
        //goToFragment(MeditationMediaPlayerFragment())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                ) = with(calendarItem) {
                    tvDateCalendarItem.text = getDayNumber(date)
                    println("tvDateCalendarItem: ${tvDateCalendarItem.text}")
                    tvDayCalendarItem.text = getDay3LettersName(date)
                    println("tvDayCalendarItem: ${tvDayCalendarItem.text}")
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
                    viewModel.setSelectedDate(date)
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
                setDates(viewModel.getFutureDatesOfCurrentMonth())
                init()
            }
            buttonSelecionar.setOnClickListener {
                if (selected) {
                    noDateSelectedWarning.hide()
                    goToFragment(AgendaFragment())
                } else {
                    noDateSelectedWarning.show()
                }
            }
        }

    }

    override fun setupUI() {}

    override fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String,Any>
    ) {
        val literalValue = numberMap.getOrElse(command) { println("Unknown number") } as Int
        selectItemOnCalendar(literalValue)
    }

    private fun selectItemOnCalendar(literalValue: Int) {
        with(viewB) {
            mainSingleRowCalendar.apply {
                clearSelection()
                if (literalValue > 5) scrollToPosition(literalValue - 1)
                select(literalValue - 1)
            }
        }
    }
}
