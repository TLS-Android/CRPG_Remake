package com.tiagosantos.crpg_remake.ui.agenda.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.TimelineView
import com.tiagosantos.common.ui.extension.invisible
import com.tiagosantos.common.ui.model.ChosenMealDish
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

/**
 * Recycler view should not observe LiveData changes directly.
 * **/
class TimeLineAdapter(
    private var mAttributes: TimelineAttributes,
    private val ctx: Context,
) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var mFeedList = mutableListOf<Event>()
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
        return TimeLineViewHolder(view, viewType)
    }

    fun submitList(newData: MutableList<Event>) {
        mFeedList.clear()
        mFeedList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]
        setContentDescription(holder,timeLineModel)
        setupTimeLine(holder,timeLineModel)

        with(_binding!!){
            when (timeLineModel.eventType) {
                ACTIVITY -> {
                    cardBackgroundImage.setBackgroundResource(R.drawable.crpg_background)
                    cardCenterIcon.setBackgroundResource(R.drawable.maos)
                    textTimelineTitle.text = "ACTIVIDADE"
                    textTimelineInfo.text = timeLineModel.eventInfo!!.uppercase(Locale.getDefault())
                }
                MEAL -> {
                    cardBackgroundImage.setBackgroundResource(R.drawable.background_dieta)
                    cardCenterIcon.setBackgroundResource(R.drawable.meal_icon)

                    textTimelineTitle.text = if (timeLineModel.mealChoice.mealType != null) {
                        when (timeLineModel.eventTitle){
                            "ALMOÇO" -> selectLunchText
                            "JANTAR" -> selectDinnerText
                            else -> { EMPTY_STRING }
                        }
                    } else { timeLineModel.mealChoice.mealType.toString() }

                    textTimelineInfo.text = if (timeLineModel.mealChoice.chosenMealDish != null) {
                        when (timeLineModel.mealChoice.chosenMealDish) {
                            ChosenMealDish.MEAT -> selectLunchText
                            ChosenMealDish.FISH -> selectDinnerText
                            else -> { EMPTY_STRING}
                        }
                    } else { timeLineModel.mealChoice.chosenMealDish.toString() }
                }
                else -> { println("nothing happens") }
            }
        }

        onCardClicked(holder, position)

    }

    private fun onCardClicked(holder: TimeLineViewHolder, position: Int) {
        with(_binding!!) {
            card.setOnClickListener {
                id = mFeedList[position].eventTitle.toString()
                tipo = mFeedList[position].eventType

                when (tipo) {
                    ACTIVITY -> MaterialAlertDialogBuilder(
                        ctx,
                        android.R.style.Theme_Material_Dialog_Alert
                    ).setTitle(mFeedList[position].eventTitle)
                    .setMessage(mFeedList[position].eventInfo)
                    .setNegativeButton("Fechar") { _, _ -> }.show()

                    MEAL -> {
                        Bundle().apply {
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
        concatTime = timeLineModel.timestampData.startTime + timeLineModel.timestampData.endTime

        with(_binding!!){
            if (overlapArray.contains(concatTime)) {
                timeline.marker.setVisible(false, false)
                textTimelineStartTime.invisible()
                textTimelineEndTime.invisible()
            } else {
                overlapArray.add(concatTime)
                timeline.setMarker(ContextCompat.getDrawable(
                    ctx,
                    R.drawable.ic_marker_active
                ), mAttributes.markerColor)
            }

            if (timeLineModel.timestampData.date.isNotEmpty()) {
                textTimelineDate.setVisible()
                textTimelineDate.text = timeLineModel.timestampData.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
            } else textTimelineDate.setGone()

            if (timeLineModel.timestampData.startTime.isNotEmpty()) {
                val newStartTime = timeLineModel.timestampData.startTime.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                textTimelineStartTime.apply { setVisible(); text = newStartTime }
            } else textTimelineStartTime.setGone()

            if (timeLineModel.timestampData.endTime.isNotEmpty()) {
                val newEndTime = timeLineModel.timestampData.endTime.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                textTimelineEndTime.apply { setVisible(); text = newEndTime }
            } else textTimelineEndTime.setGone()

            if (timeLineModel.eventTitle!!.isNotEmpty()) textTimelineTitle.text = timeLineModel.eventTitle
            if (timeLineModel.eventInfo!!.isNotEmpty()) textTimelineInfo.text = timeLineModel.eventInfo

        }

    }

    private fun setContentDescription(
        holder: TimeLineViewHolder,
        timeLineModel: Event,
    ) {
        with(holder.itemView) {
            contentDescription = when (timeLineModel.eventType) {
                ACTIVITY -> {
                     "Actividade" +
                            "com o título ${timeLineModel.eventTitle} e tendo como descrição ${timeLineModel.eventInfo}," +
                            " que irá" +
                            " começar às ${timeLineModel.timestampData.startTime}. Clique para obter mais informações"
                }

                MEAL -> {
                        if (timeLineModel.mealChoice.chosenMealDish != null) chosenMealisBlankText
                        else {
                            "Refeição selecionada, o prato escolhido " +
                                    "foi ${timeLineModel.mealChoice.chosenMealDish}"
                        }
                }
            }
        }

    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = _binding!!.textTimelineDate
        val title = _binding!!.textTimelineTitle
        val info = _binding!!.textTimelineInfo
        val startTime = _binding!!.textTimelineStartTime
        val endTime = _binding!!.textTimelineEndTime
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
