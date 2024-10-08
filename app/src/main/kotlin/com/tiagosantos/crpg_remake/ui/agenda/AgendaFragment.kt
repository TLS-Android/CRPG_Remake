package com.tiagosantos.crpg_remake.ui.agenda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.plataforma.crpg.TimelineView
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.Orientation
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
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

/**
 * Android doesn't allow using an overloaded constructor for fragments anymore.
 * You must use the newInstance method.´
 *
 * You cannot call requireContext() at a property initialization site because properties
 * are initialized while the Fragment instance is still being constructed,
 * which is before it can possibly be attached to a context.
 */
@FragmentWithArgs
class AgendaFragment : BaseModalFragment<FragmentAgendaBinding>() {

    @delegate:Arg
    override val settings by lazy {
        FragmentSettings(
            appBarTitle = getString(R.string.agenda),
            sharedPreferencesBooleanName = getString(R.string.agendaHasRun),
            showBackButton = false,
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(
            contextualHelp = R.string.contextual_agenda,
            isSpeaking = true,
        )
    }

    private val viewModel: AgendaViewModel by viewModels()

    private lateinit var mAttributes: TimelineAttributes
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapterGlobal : TimeLineAdapter

    private var mFeedList = mutableListOf<Event>()

    /**
     You must .observe() in onCreateView().
    Not just for this specific case, but in general you will never need to call .observe() in any other places if you are using LiveData correctly.
    viewLifecycleOwner is the correct LifecycleOwner to use for observing a LiveData. (Unless you create a custom one.)
    Depends on the use case but generally instantiate one adapter per RecyclerView, even if your data changes over time.
    You should swap the data, not the whole adapter.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun setupUI() {
        setAttributes()
        initRecyclerView()
        observeLifecycleEvents()
        mAttributes.orientation = Orientation.VERTICAL
    }

    private fun initRecyclerView() {
        adapterGlobal = TimeLineAdapter(mAttributes, requireContext())
        initAdapter()
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

    private fun initAdapter() {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(activity, HORIZONTAL, false)
        } else {
            LinearLayoutManager(activity, VERTICAL, false)
        }

        viewB.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = adapterGlobal
        }

    }

    override fun onResume() {
        super.onResume()
        updateMAttributes()
    }

    private fun updateMAttributes() =  mAttributes.let {
        it.onOrientationChanged =  { oldValue, newValue ->
            if (oldValue != newValue) initRecyclerView()
        }
        it.orientation = Orientation.VERTICAL
    }

    private fun addItemsToFeed() = viewModel.getEventCollection()

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
        viewModel.liveDataList.observe(viewLifecycleOwner) { newEvent ->
            mFeedList = newEvent
            println("mFeedList = $mFeedList")
            adapterGlobal.submitList(mFeedList)
        }
        addItemsToFeed()
    }
}
