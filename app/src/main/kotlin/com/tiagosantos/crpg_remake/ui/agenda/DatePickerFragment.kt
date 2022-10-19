package com.tiagosantos.crpg_remake.ui.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.tiagosantos.common.ui.singlerowcalendar.calendar.SingleRowCalendar
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentDatePickerBinding
import java.util.*

class DatePickerFragment(ttsSettings: TTSFragmentSettings) :
    BaseModalFragment<FragmentDatePickerBinding>(
        layoutId = R.layout.fragment_date_picker,
        FragmentSettings(
            appBarTitle = "ESCOLHER DATA",
            sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
        ), TTSFragmentSettings(
"Por favor selecione um dia movendo os quadrados amarelos para a esquerda " +
        "e direita e premindo aquele que pretender selecionar"
        )
) {
    private lateinit var view: FragmentDatePickerBinding
    private var selected = false
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val vm: AgendaViewModel by viewModels()
        calendar.time = Date()
        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean,
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance().apply { time = date }
                if (!isSelected) view. tvDate.text =
                    getString(R.string.nenhum_dia_selecionado_msg)

                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.selected_calendar_item
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            R.layout.calendar_item
                        }
                    }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean,
            ) {
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views
                holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text =
                    DateUtils.getDay3LettersName(date)
            }
        }

        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            override fun whenSelectionChanged(
                isSelected: Boolean,
                position: Int,
                date: Date
            ) {
                view.tvDate.text = buildString {
        append("${DateUtils.getDayName(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }},")
        append(" ${DateUtils.getDayNumber(date)} de ${
            DateUtils.getMonthName(date)
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
        }")
    }
                view.tvDay.text = DateUtils.getDayName(date)
                vm.selectedDate = DateUtils.getDayNumber(date) + DateUtils
                    .getMonthNumber(date) + DateUtils.getYear(date)
                super.whenSelectionChanged(isSelected, position, date)
                selected = isSelected
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance().apply { time = date }
                // saturday and sunday are disabled as CRPG is not open on these days
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.SATURDAY -> false
                    Calendar.SUNDAY -> false
                    else -> true
                }
            }
        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        view.buttonSelecionar.setOnClickListener {
            if (selected) {
                view.noDateSelectedWarning.visibility = GONE
                val fragment: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(ent = AgendaFragment()
                    val fragmentManagerR.id.nav_host_fragment, fragment, "Agenda")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                onDestroy()
            } else {
                no_date_selected_warning.visibility = VISIBLE
            }
        }

       return view.root
    }

    private fun performActionWithVoiceCommand(
        command: String,
        singleRowCalendar: SingleRowCalendar
    ) {
        val literalValue = mapOf(
            "um" to 1, "dois" to 2, "três" to 3, "quatro" to 4,
            "cinco" to 5, "seis" to 6, "sete" to 7, "oito" to 8, "nove" to 9, "dez" to 10
        ).getOrElse(command) { println("Número não presente na lista") } as Int

        singleRowCalendar.let {
            it.clearSelection()
            if (literalValue > 5)it.scrollToPosition(literalValue - 1)
            it.select(literalValue - 1)
        }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }
}
