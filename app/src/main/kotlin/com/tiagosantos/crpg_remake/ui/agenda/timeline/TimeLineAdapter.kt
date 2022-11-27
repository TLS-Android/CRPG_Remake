package com.tiagosantos.crpg_remake.ui.agenda.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.TimelineView
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.model.EventType
import com.tiagosantos.common.ui.model.EventType.*
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.Constants.selectLunchText
import com.tiagosantos.common.ui.utils.Constants.selectDinnerText
import com.tiagosantos.common.ui.utils.Constants.chosenMealisBlankText
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.ItemTimelineBinding
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.formatDateTime
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.setGone
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.setVisible
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
import java.util.*

/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(
    private val mFeedList: LiveData<MutableList<Event>?>,
    private var mAttributes: TimelineAttributes,
    private val ctx: Context,
) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var overlapArray = mutableListOf(EMPTY_STRING)
    private var concatTime = EMPTY_STRING

    private lateinit var id: String
    private lateinit var tipo: EventType

    private lateinit var mLayoutInflater: LayoutInflater

    private var _binding: ItemTimelineBinding? = null

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::mLayoutInflater.isInitialized) { mLayoutInflater = LayoutInflater.from(parent.context) }
        val view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false)
        //_binding = ItemTimelineBinding.inflate(inflater, container, false)
        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList.value!![position]

        setContentDescription(holder,timeLineModel)
        setupTimeLine(holder,timeLineModel)

        with(_binding!!){
            when (timeLineModel.type) {
                ACTIVITY -> {
                    cardBackgroundImage.setBackgroundResource(R.drawable.crpg_background)
                    cardCenterIcon.setBackgroundResource(R.drawable.maos)
                    textTimelineTitle.text = "ACTIVIDADE"
                    textTimelineInfo.text = timeLineModel.info.uppercase(Locale.getDefault())
                }
                MEAL -> {
                    cardBackgroundImage.setBackgroundResource(R.drawable.background_dieta)
                    cardCenterIcon.setBackgroundResource(R.drawable.meal_icon)


                    if (timeLineModel.chosen_meal.isBlank()) {
                        textTimelineInfo.text = when (timeLineModel.title){
                            "ALMOÇO" -> selectLunchText
                            "JANTAR" -> selectDinnerText
                            else -> { "ola" }
                        }.toString()
                    }

                    when (timeLineModel.title) {
                        "ALMOÇO" -> if (timeLineModel.chosen_meal.isBlank()) {
                            textTimelineInfo.text = selectLunchText
                        } else {
                            textTimelineInfo.text = timeLineModel.chosen_meal
                        }

                        "JANTAR" -> if (timeLineModel.chosen_meal.isBlank()) {
                            textTimelineInfo.text = selectDinnerText
                        } else {
                            textTimelineInfo.text = timeLineModel.chosen_meal
                        }
                    }
                }
                else -> { println("nothing happens") }
            }
        }

        onCardClicked(holder, position)

        fun performActionWithVoiceCommand(command: String) {
            with(_binding!!){
                when {
                    command.contains("Escolher Almoço", true) && id == "Almoço" ||
                            command.contains("Escolher Jantar", true) && id == "Jantar" ->
                        card.performClick()

                    command.contains(id, true) && tipo == ACTIVITY ->
                        card.performClick()
                    else -> { println ("yo")}
                }
            }

        }

    }

    private fun onCardClicked(holder: TimeLineViewHolder, position: Int) {

        // onClick on a card open pop up or go to Meal
        with(_binding!!){
            card.setOnClickListener {
                id = mFeedList.value!![position].title.toString()
                tipo = mFeedList.value!![position].type

                when (tipo) {
                    ACTIVITY -> {
                        MaterialAlertDialogBuilder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                            .setTitle(mFeedList.value!![position].title)
                            .setMessage(mFeedList.value!![position].info)
                            .setNegativeButton("Fechar") { _, _ -> }.show()
                    }

                    MEAL -> {
                        val bundle = Bundle().apply {
                            when (id) {
                                "ALMOÇO" -> putBoolean("isLunch", true)
                                "JANTAR" -> putBoolean("isLunch", false)
                            }
                        }
                    }
                }
            }

        }

    }

    private fun setupTimeLine(
        holder: TimeLineViewHolder,
        timeLineModel: Event,
    ) {

        concatTime = timeLineModel.start_time + timeLineModel.end_time

        with(_binding!!){
            if (overlapArray.contains(concatTime)) {
                timeline.marker.setVisible(false, false)
                textTimelineStartTime.visibility = INVISIBLE
                textTimelineEndTime.visibility = INVISIBLE
            } else {
                overlapArray.add(concatTime)
                timeline.setMarker(ContextCompat.getDrawable(
                    ctx,
                    R.drawable.ic_marker_active
                ), mAttributes.markerColor)
            }

            if (timeLineModel.date.isNotEmpty()) {
                textTimelineDate.setVisible()
                textTimelineDate.text = timeLineModel.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
            } else textTimelineDate.setGone()

            if (timeLineModel.start_time.isNotEmpty()) {
                val newStartTime = timeLineModel.start_time.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                textTimelineStartTime.apply { setVisible(); text = newStartTime }
            } else textTimelineStartTime.setGone()

            if (timeLineModel.end_time.isNotEmpty()) {
                val newEndTime = timeLineModel.end_time.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                textTimelineEndTime.apply { setVisible(); text = newEndTime }
            } else textTimelineEndTime.setGone()

            if (timeLineModel.title!!.isNotEmpty()) textTimelineTitle.text = timeLineModel.title
            if (timeLineModel.info.isNotEmpty()) textTimelineInfo.text = timeLineModel.info

        }

    }

    private fun setContentDescription(
        holder: TimeLineViewHolder,
        timeLineModel: Event,
    ) {

        with(holder.itemView) {
            contentDescription = when (timeLineModel.type) {
                ACTIVITY -> {
                     "Actividade" +
                            "com o título ${timeLineModel.title} e tendo como descrição ${timeLineModel.info}," +
                            " que irá" +
                            " começar às ${timeLineModel.start_time}. Clique para obter mais informações"
                }

                MEAL -> {
                        if (timeLineModel.chosen_meal.isBlank()) chosenMealisBlankText
                        else {
                            "Refeição selecionada, o prato escolhido " +
                                    "foi ${timeLineModel.chosen_meal}"
                        }

                }
            }
        }

    }

    override fun getItemCount() = mFeedList.value!!.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = _binding!!.textTimelineDate
        val title = _binding!!.textTimelineTitle
        val info = _binding!!.textTimelineInfo
        val startTime = _binding!!.textTimelineStartTime
        val end_time = _binding!!.textTimelineEndTime
        val timeline = _binding!!.timeline

        init {
            timeline.initLine(viewType)
            timeline.markerSize = mAttributes.markerSize
            timeline.setMarkerColor(mAttributes.markerColor)
            timeline.isMarkerInCenter = mAttributes.markerInCenter
            timeline.markerPaddingLeft = mAttributes.markerLeftPadding
            timeline.markerPaddingTop = mAttributes.markerTopPadding
            timeline.markerPaddingRight = mAttributes.markerRightPadding
            timeline.markerPaddingBottom = mAttributes.markerBottomPadding
            timeline.linePadding = mAttributes.linePadding
            timeline.lineWidth = mAttributes.lineWidth
            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
            timeline.lineStyle = mAttributes.lineStyle
            timeline.lineStyleDashLength = mAttributes.lineDashWidth
            timeline.lineStyleDashGap = mAttributes.lineDashGap
        }
    }

}
