package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.TimelineView
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.Orientation
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentAgendaBinding
import com.tiagosantos.crpg_remake.ui.agenda.timeline.TimeLineAdapter
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.dpToPx
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.getColorCompat
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.setGone
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.setVisible

class AgendaFragment : BaseModalFragment<FragmentAgendaBinding>(
    layoutId = R.layout.fragment_agenda,
    FragmentSettings(
        appBarTitle = R.string.title_agenda,
        sharedPreferencesBooleanName = R.string.agendaHasRun.toString(),
    ),
    ttsSettings = TTSSettings(
        "Selecione a janela que pretender para obter mais informaçoes",
        isSpeaking = true
    ),
    srSettings = SRSettings(
        isListening = false,
        actionMap = null
    )
) {

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    private val agendaVM: AgendaViewModel by viewModels()

    private var ctx = context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAttributes = TimelineAttributes(
            markerSize = dpToPx(20f),
            markerColor = getColorCompat(R.color.GreyedBlue),
            markerInCenter = true,
            markerLeftPadding = dpToPx(0f),
            markerTopPadding = dpToPx(0f),
            markerRightPadding = dpToPx(0f),
            markerBottomPadding = dpToPx(0f),
            linePadding = dpToPx(2f),
            startLineColor = getColorCompat(R.color.colorAccent),
            endLineColor = getColorCompat(R.color.colorAccent),
            lineStyle = TimelineView.LineStyle.NORMAL,
            lineWidth = dpToPx(2f),
            lineDashWidth = dpToPx(4f),
            lineDashGap = dpToPx(2f)
        )
        val ctx = context
        setDataListItemsWithoutPopulate()
        if (ctx != null) {
            initRecyclerView(ctx)
        }

        updateMAttributes()
    }


    private fun initRecyclerView(ctx: Context) {
        initAdapter(ctx)
        with(viewB){
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("LongLogTag")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (recyclerView.getChildAt(0).top < 0)
                        dropshadow.setVisible() else dropshadow.setGone()
                }
            })
        }

    }

    private fun initAdapter(ctx: Context) {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }

        viewB.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(agendaVM.mDataList, mAttributes, ctx)
        }
    }

    override fun onResume() {
        super.onResume()
        setDataListItemsWithoutPopulate()
        ctx?.let { initRecyclerView(it) }
        updateMAttributes()
    }

    private fun updateMAttributes() =  mAttributes.let { it.onOrientationChanged =  { oldValue, newValue ->
        if (oldValue != newValue) initRecyclerView(ctx!!) }
        it.orientation = Orientation.VERTICAL
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

    private fun setDataListItemsWithoutPopulate() = agendaVM.getEventCollectionFromJSON()

}
