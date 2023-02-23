package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.TimelineView
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.Orientation
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
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
        showBackButton = false,
    ),
    ttsSettings = TTSSettings(
        "Selecione a janela que pretender para obter mais informaçoes",
        isSpeaking = true,
    ),
    srSettings = SRSettings(
        commandList = listOf("Almoço", "Jantar"),
        isListening = false,
    )
) {
    private val agendaVM: AgendaViewModel by viewModels()
    private var ctx = context

    private lateinit var mAttributes: TimelineAttributes
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapter: TimeLineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var event : MutableList<Event>

        agendaVM.liveDataList.observe(this, Observer<MutableList<Event>> { newEvent ->
            event = newEvent
            println("Event: $event")
        })

    }

    /**
     * Note that the fragment's viewLifecycleOwner
     * is only available between onCreateView and onDestroyView lifecycle methods.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //observeLifecycleEvents()
        //setAttributes()
        return viewB.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observeLifecycleEvents()
        //setAttributes()
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
            adapter = TimeLineAdapter(mAttributes, ctx)
        }

    }

    override fun onResume() {
        super.onResume()
        //setDataListItemsWithoutPopulate()
        //ctx?.let { initRecyclerView(it) }
        //updateMAttributes()
    }

    private fun updateMAttributes() =  mAttributes.let { it.onOrientationChanged =  { oldValue, newValue ->
        if (oldValue != newValue) initRecyclerView(ctx!!) }
        it.orientation = Orientation.VERTICAL
    }

    /**
     * Make sure the mediatorLiveData has an active observer attached.
     * addSource method checks whether
     * any active observers are attached before subscribing to the source.
     *
     *

    The first method where it is safe to access the view lifecycle is onCreateView(LayoutInflater, ViewGroup, Bundle) under the condition that you must return a non-null view (an IllegalStateException will be thrown if you access the view lifecycle but don't return a non-null view).

    The view lifecycle remains valid through the call to onDestroyView(), after which getView() will return null, the view lifecycle will be destroyed, and this method will throw an IllegalStateException. Consider using getViewLifecycleOwnerLiveData() or FragmentTransaction.runOnCommit(Runnable) to receive a callback for when the Fragment's view lifecycle is available.

     */
    private fun observeLifecycleEvents() {
        val ctx = context


        println("Hello")

        setDataListItemsWithoutPopulate()

    }

    private fun setDataListItemsWithoutPopulate() = agendaVM.getEventCollection()

    private fun setAttributes() {
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
    }
}
