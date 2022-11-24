package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.Orientation
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentAgendaBinding
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.dpToPx
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.getColorCompat

import java.util.*

class AgendaFragment : BaseModalFragment<FragmentAgendaBinding>(
    layoutId = R.layout.fragment_agenda,
    FragmentSettings(
        appBarTitle = R.string.title_agenda,
        sharedPreferencesBooleanName = R.string.agendaHasRun.toString(),
    )
) {

    private val _mDataList = MutableLiveData<ArrayList<Event>?>()
    val mDataList: LiveData<ArrayList<Event>?> = _mDataList

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onResume() {
        super.onResume()

        setDataListItemsWithoutPopulate()
        val ctx = context
        if (ctx != null) {
            initRecyclerView(ctx)
        }

        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView(ctx!!)
        }
        mAttributes.orientation = Orientation.VERTICAL
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "AGENDA"
        mAttributes = TimelineAttributes(
            markerSize = dpToPx(20f),
            markerColor = getColorCompat(R.color.material_grey_500),
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

        mAttributes.onOrientationChanged = { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView(ctx!!)
        }

        mAttributes.orientation = Orientation.VERTICAL
    }

    private fun setDataListItemsWithoutPopulate() {
        val eventViewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        mDataList = eventViewModel.getEventCollectionFromJSONWithoutPopulate()
        mDataList.sortBy { it.start_time }
    }

    private fun initRecyclerView(ctx: Context) {
        initAdapter(ctx)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.getChildAt(0).top < 0) dropshadow.setVisible() else dropshadow.setGone()
            }
        })
    }

    private fun initAdapter(ctx: Context) {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(mDataList, mAttributes, ctx)
        }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}
