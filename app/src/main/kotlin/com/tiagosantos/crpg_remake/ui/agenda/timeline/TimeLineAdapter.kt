package com.tiagosantos.crpg_remake.ui.agenda.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.TimelineView
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.model.EventType
import com.tiagosantos.common.ui.model.EventType.*
import com.tiagosantos.common.ui.model.TimelineAttributesBackup
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.formatDateTime
import java.util.*

/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(
    private val mFeedList: List<Event>,
    private var mAttributes: TimelineAttributesBackup,
    private val ctx: Context,
) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var overlapArray = mutableListOf(EMPTY_STRING)
    private var concatTime = EMPTY_STRING

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if (!::mLayoutInflater.isInitialized) { mLayoutInflater = LayoutInflater.from(parent.context) }
        val view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false)
        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        setContentDescription(holder,timeLineModel)
        setupTimeLine(holder,timeLineModel)

        with(holder.itemView){
            when (timeLineModel.type) {
                ACTIVITY -> {
                    card_background_image.setBackgroundResource(R.drawable.crpg_background)
                    card_center_icon.setBackgroundResource(R.drawable.maos)
                    text_timeline_title.text = "ACTIVIDADE"
                    text_timeline_info.text = timeLineModel.info.uppercase(Locale.getDefault())
                }
                MEAL -> {
                    card_background_image.setBackgroundResource(R.drawable.background_dieta)
                    card_center_icon.setBackgroundResource(R.drawable.meal_icon)

                    when (timeLineModel.title) {
                        "ALMOÇO" -> if (timeLineModel.chosen_meal.isBlank()) {
                            text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR ALMOÇO"
                        } else {
                            text_timeline_info.text = timeLineModel.chosen_meal
                        }

                        "JANTAR" -> if (timeLineModel.chosen_meal.isBlank()) {
                            text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR JANTAR"
                        } else {
                            text_timeline_info.text = timeLineModel.chosen_meal
                        }
                    }
                }
                else -> { println("nothing happens") }
            }
        }

        var id: String
        var tipo: EventType

        // onClick on a card open pop up or go to Meal
        holder.itemView.card.setOnClickListener {
            id = mFeedList[position].title.toString()
            tipo = mFeedList[position].type

            when (tipo) {
                ACTIVITY -> {
                    MaterialAlertDialogBuilder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                        .setTitle(mFeedList[position].title)
                        .setMessage(mFeedList[position].info)
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

    private fun setupTimeLine(
        holder: TimeLineAdapter.TimeLineViewHolder,
        timeLineModel: Event) {

        concatTime = timeLineModel.start_time + timeLineModel.end_time

        with(holder){
            if (overlapArray.contains(concatTime)) {
                timeline.marker.setVisible(false, false)
                itemView.text_timeline_start_time.visibility = INVISIBLE
                itemView.text_timeline_end_time.visibility = INVISIBLE
            } else {
                overlapArray.add(concatTime)
                timeline.setMarker(ContextCompat.getDrawable(itemView.context, R.drawable.ic_marker_active), mAttributes.markerColor)
            }

            if (timeLineModel.date.isNotEmpty()) {
                date.setVisible()
                date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
            } else date.setGone()

            if (timeLineModel.start_time.isNotEmpty()) {
                startTime.setVisible()
                var newStartTime = timeLineModel.start_time.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                startTime.text = newStartTime
            } else startTime.setGone()

            if (timeLineModel.end_time.isNotEmpty()) {
                var newEndTime = timeLineModel.end_time.apply { "${substring(0, 2)} : ${substring(2, 4)}" }
                timeLineModel.end_time.run { setVisible(); this.text = newEndTime }
                end_time.text = newEndTime
            } else startTime.setGone()

            if (timeLineModel.title!!.isNotEmpty()) title.text = timeLineModel.title
            if (timeLineModel.info.isNotEmpty()) info.text = timeLineModel.info

        }

    }

    private fun setContentDescription(
        holder: TimeLineAdapter.TimeLineViewHolder,
        timeLineModel: Event) {

        with(holder.itemView) {
            when (timeLineModel.type) {
                ACTIVITY -> {
                    contentDescription = "Actividade" +
                            "com o título ${timeLineModel.title} e tendo como descrição ${timeLineModel.info}," +
                            " que irá" +
                            " começar às ${timeLineModel.start_time}. Clique para obter mais informações"
                }

                MEAL -> {
                    contentDescription = if (timeLineModel.chosen_meal.isBlank()) {
                        "Nenhuma refeição selecionada, clique para selecionar" +
                                "a sua refeição"
                    } else {
                        "Refeição selecionada, o prato escolhido " +
                                "foi ${timeLineModel.chosen_meal}"
                    }
                }
            }
        }

    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.text_timeline_date!!
        val title = itemView.text_timeline_title!!
        val info = itemView.text_timeline_info!!
        val startTime = itemView.text_timeline_start_time!!
        val end_time = itemView.text_timeline_end_time!!
        val timeline = itemView.timeline!!

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

    fun performActionWithVoiceCommand(
        holder: TimeLineViewHolder,
        command: String,
        actionMap: Map<String, Any>
    ) {
        when {
            command.contains("Escolher Almoço", true) && id == "Almoço" ||
                    command.contains("Escolher Jantar", true) && id == "Jantar" ->
                holder.itemView.card.performClick()
            command.contains(id, true) && tipo == ACTIVITY ->
                holder.itemView.card.performClick()
        }
    }
}
