package com.tiagosantos.crpg_remake.ui.agenda.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.model.EventType
import com.tiagosantos.common.ui.model.TimelineAttributesBackup
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.ui.meals.MealsFragment
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

        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        val view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false)

        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        when (timeLineModel.type) {
            EventType.ACTIVITY -> {
                holder.itemView.contentDescription = "Actividade" +
                    "com o título ${timeLineModel.title} e tendo como descrição ${timeLineModel.info}," +
                    " que irá" +
                    " começar às ${timeLineModel.start_time}. Clique para obter mais informações"
            }

            EventType.MEAL -> {
                if (timeLineModel.chosen_meal.isNullOrBlank()) {
                    holder.itemView.contentDescription = "Nenhuma refeição selecionada, clique para selecionar" +
                        "a sua refeição"
                } else {
                    holder.itemView.contentDescription = "Refeição selecionada, o prato escolhido " +
                        "foi ${timeLineModel.chosen_meal}"
                }
            }

            EventType.TRANSPORTS -> {
                holder.itemView.contentDescription = "Aceder à secção de transportes"
            }
        }

        concatTime = timeLineModel.start_time + timeLineModel.end_time

        if (overlapArray.contains(concatTime)) {
            holder.timeline.marker.setVisible(false, false)
            holder.itemView.text_timeline_start_time.visibility = INVISIBLE
            holder.itemView.text_timeline_end_time.visibility = INVISIBLE
        } else {
            overlapArray.add(concatTime)
            holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker_active), mAttributes.markerColor)
        }

        if (timeLineModel.date.isNotEmpty()) {
            holder.date.setVisible()
            holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd", "dd-MMM-yyyy")
        } else
            holder.date.setGone()

        if (timeLineModel.start_time.isNotEmpty()) {
            holder.startTime.setVisible()
            var newStartTime: String = timeLineModel.start_time
            newStartTime = "${newStartTime.substring(0, 2)} : ${newStartTime.substring(2, 4)}"
            holder.startTime.text = newStartTime
        } else
            holder.startTime.setGone()

        if (timeLineModel.end_time.isNotEmpty()) {
            holder.end_time.setVisible()
            var newEndTime: String = timeLineModel.end_time
            newEndTime = "${newEndTime.substring(0, 2)} : ${newEndTime.substring(2, 4)}"
            holder.end_time.text = newEndTime
        } else holder.startTime.setGone()

        if (timeLineModel.title.isNotEmpty()) {
            holder.title.text = timeLineModel.title
        }

        if (timeLineModel.info.isNotEmpty()) {
            holder.info.text = timeLineModel.info
        }

        when (timeLineModel.type) {
            EventType.ACTIVITY -> {
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.crpg_background)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.maos)
                holder.itemView.text_timeline_title.text = "ACTIVIDADE"
                holder.itemView.text_timeline_info.text = timeLineModel.info.uppercase(Locale.getDefault())
            }
            EventType.MEAL -> {
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.background_dieta)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.meal_icon)

                when (timeLineModel.title) {
                    "ALMOÇO" -> if (timeLineModel.chosen_meal.isBlank()) {
                        holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR ALMOÇO"
                    } else {
                        holder.itemView.text_timeline_info.text = timeLineModel.chosen_meal
                    }

                    "JANTAR" -> if (timeLineModel.chosen_meal.isBlank()) {
                        holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA SELECIONAR JANTAR"
                    } else {
                        holder.itemView.text_timeline_info.text = timeLineModel.chosen_meal
                    }
                }
            }
            EventType.TRANSPORTS -> {
                holder.itemView.text_timeline_info.text = "CLIQUE AQUI PARA MAIS INFORMAÇÕES"
                holder.itemView.card_background_image.setBackgroundResource(R.drawable.stcp_background)
                holder.itemView.card_center_icon.setBackgroundResource(R.drawable.bus_icon)
            }
        }

        var id: String
        var tipo: EventType

        // onClick on a card open pop up or go to Meal or Transport Fragment
        holder.itemView.card.setOnClickListener {
            id = mFeedList[position].title
            tipo = mFeedList[position].type

            when (tipo) {
                EventType.ACTIVITY -> {
                    MaterialAlertDialogBuilder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                        .setTitle(mFeedList[position].title)
                        .setMessage(mFeedList[position].info)
                        .setNegativeButton("Fechar") { _, _ -> }.show()
                }

                EventType.MEAL -> {
                    val bundle = Bundle()

                    when (id) {
                        "ALMOÇO" -> {
                            bundle.putBoolean("isLunch", true)
                        }
                        "JANTAR" -> {
                            bundle.putBoolean("isLunch", false)
                        }
                    }

                    val fragment: Fragment = MealsFragment()
                    fragment.arguments = bundle
                    val fragmentManager: FragmentManager =
                        (ctx as AppCompatActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()
                    fragmentManager.findFragmentByTag("Agenda")
                        ?.let { it1 -> fragmentTransaction.remove(it1) }
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }

            fun performActionWithVoiceCommand(command: String, actionMap: Map<String, Any>) {
                when {
                    command.contains("Escolher Almoço", true) && id == "Almoço" ||
                            command.contains("Escolher Jantar", true) && id == "Jantar" ->
                        holder.itemView.card.performClick()
                    command.contains(id, true) && tipo == EventType.ACTIVITY ->
                        holder.itemView.card.performClick()
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
}
