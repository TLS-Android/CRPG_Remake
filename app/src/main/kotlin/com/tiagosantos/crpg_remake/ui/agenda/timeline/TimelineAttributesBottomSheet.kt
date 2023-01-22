package com.tiagosantos.crpg_remake.ui.agenda.timeline

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.plataforma.crpg.TimelineView
import com.thebluealliance.spectrum.SpectrumDialog
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.BottomSheetOptionsBinding
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.Orientation
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.TimelineAttributes
import com.tiagosantos.crpg_remake.ui.agenda.timeline.widgets.BorderedCircle
import com.tiagosantos.crpg_remake.ui.agenda.timeline.widgets.RoundedCornerBottomSheet
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

@Parcelize
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TimelineAttributesBottomSheet : RoundedCornerBottomSheet(), Parcelable {

    interface Callbacks {
        fun onAttributesChanged(attributes: TimelineAttributes)
    }

    companion object {
        private const val EXTRA_ATTRIBUTES = "EXTRA_ATTRIBUTES"

        fun showDialog(fragmentManager: FragmentManager, attributes: TimelineAttributes, callbacks: Callbacks) {
            val dialog = TimelineAttributesBottomSheet()
            dialog.arguments = bundleOf(
                EXTRA_ATTRIBUTES to attributes
            )
            dialog.setCallback(callbacks)
            dialog.show(fragmentManager, "[TIMELINE_ATTRIBUTES_BOTTOM_SHEET]")
        }
    }

    @IgnoredOnParcel
    private var mCallbacks: Callbacks? = null
    @IgnoredOnParcel
    private lateinit var mAttributes: TimelineAttributes
    @IgnoredOnParcel
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    @IgnoredOnParcel
    private lateinit var optionsView: BottomSheetOptionsBinding


    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val bottomSheet = dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            mBottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            mBottomSheetBehavior?.peekHeight = view?.measuredHeight!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.bottom_sheet_options, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attributes = (requireArguments().getParcelable(EXTRA_ATTRIBUTES, TimelineAttributes::class.java)!!)
        mAttributes = attributes.copy()

        with(optionsView){

            textAttributesHeading.setOnClickListener { dismiss() }

            // orientation
            rgOrientation.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_horizontal -> {
                        mAttributes.orientation = Orientation.HORIZONTAL
                    }
                    R.id.rb_vertical -> {
                        mAttributes.orientation = Orientation.VERTICAL
                    }
                }
            }

            rgOrientation.check(if (mAttributes.orientation == Orientation.VERTICAL) R.id.rb_vertical else R.id.rb_horizontal)

        }

        with(optionsView.layoutMarker){

            // marker
            seekMarkerSize.progress = mAttributes.markerSize
            imageMarkerColor.mFillColor = mAttributes.markerColor
            checkboxMarkerInCenter.isChecked = mAttributes.markerInCenter
            seekMarkerLeftPadding.progress = mAttributes.markerLeftPadding
            seekMarkerTopPadding.progress = mAttributes.markerTopPadding
            seekMarkerRightPadding.progress = mAttributes.markerRightPadding
            seekMarkerBottomPadding.progress = mAttributes.markerBottomPadding
            seekMarkerLinePadding.progress = mAttributes.linePadding

            checkboxMarkerInCenter.setOnCheckedChangeListener { _, isChecked ->
                mAttributes.markerInCenter = isChecked
            }

            imageMarkerColor.setOnClickListener { showColorPicker(mAttributes.markerColor, imageMarkerColor) }

            seekMarkerSize.setOnProgressChangeListener(progressChangeListener)
            seekMarkerLeftPadding.setOnProgressChangeListener(progressChangeListener)
            seekMarkerTopPadding.setOnProgressChangeListener(progressChangeListener)
            seekMarkerRightPadding.setOnProgressChangeListener(progressChangeListener)
            seekMarkerBottomPadding.setOnProgressChangeListener(progressChangeListener)
            seekMarkerLinePadding.setOnProgressChangeListener(progressChangeListener)

        }

        // line
        Log.e(" mAttributes.lineWidth", "${ mAttributes.lineWidth}")

        with(optionsView.layoutLine) {

            seekLineWidth.progress = mAttributes.lineWidth
            imageStartLineColor.mFillColor = mAttributes.startLineColor
            imageEndLineColor.mFillColor = mAttributes.endLineColor

            imageStartLineColor.setOnClickListener {
                showColorPicker(
                    mAttributes.startLineColor,
                    imageStartLineColor
                )
            }
            imageEndLineColor.setOnClickListener {
                showColorPicker(
                    mAttributes.endLineColor,
                    imageEndLineColor
                )
            }

            when (mAttributes.lineStyle) {
                TimelineView.LineStyle.NORMAL -> spinnerLineType.setSelection(0)
                TimelineView.LineStyle.DASHED -> spinnerLineType.setSelection(1)
            }

            spinnerLineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getItemAtPosition(position).toString()) {
                        "Normal" -> mAttributes.lineStyle = TimelineView.LineStyle.NORMAL
                        "Dashed" -> mAttributes.lineStyle = TimelineView.LineStyle.DASHED
                        else -> {
                            mAttributes.lineStyle = TimelineView.LineStyle.NORMAL
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }




        seekLineDashWidth.progress = mAttributes.lineDashWidth
        seekLineDashGap.progress = mAttributes.lineDashGap

        seekLineWidth.setOnProgressChangeListener(progressChangeListener)
        seekLineDashWidth.setOnProgressChangeListener(progressChangeListener)
        seekLineDashGap.setOnProgressChangeListener(progressChangeListener)

    }

    optionsView.buttonApply.setOnClickListener {
        mCallbacks?.onAttributesChanged(mAttributes)
        dismiss()
    }

    }

    private fun showColorPicker(selectedColor: Int, colorView: BorderedCircle) {
        SpectrumDialog.Builder(requireContext())
            .setColors(R.array.colors)
            .setSelectedColor(selectedColor)
            .setDismissOnColorSelected(true)
            .setOutlineWidth(1)
            .setOnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    colorView.mFillColor = color

                    when (colorView.id) {
                        R.id.image_marker_color -> { mAttributes.markerColor = color }
                        R.id.image_start_line_color -> { mAttributes.startLineColor = color }
                        R.id.image_end_line_color -> { mAttributes.endLineColor = color }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }.build().show(childFragmentManager, "ColorPicker")
    }

    @IgnoredOnParcel
    private var progressChangeListener: DiscreteSeekBar.OnProgressChangeListener = object : DiscreteSeekBar.OnProgressChangeListener {

        override fun onProgressChanged(discreteSeekBar: DiscreteSeekBar, value: Int, b: Boolean) {

            Log.d("onProgressChanged", "value->$value")
            when (discreteSeekBar.id) {
                R.id.seek_marker_size -> { mAttributes.markerSize = value }
                R.id.seek_marker_left_padding -> { mAttributes.markerLeftPadding = value }
                R.id.seek_marker_top_padding -> { mAttributes.markerTopPadding = value }
                R.id.seek_marker_right_padding -> { mAttributes.markerRightPadding = value }
                R.id.seek_marker_bottom_padding -> { mAttributes.markerBottomPadding = value }
                R.id.seek_marker_line_padding -> { mAttributes.linePadding = value }
                R.id.seek_line_width -> { mAttributes.lineWidth = value }
                R.id.seek_line_dash_width -> { mAttributes.lineDashWidth = value }
                R.id.seek_line_dash_gap -> { mAttributes.lineDashGap = value }
            }
        }

        override fun onStartTrackingTouch(discreteSeekBar: DiscreteSeekBar) {
        }

        override fun onStopTrackingTouch(discreteSeekBar: DiscreteSeekBar) {
        }
    }

    private fun setCallback(callbacks: Callbacks) {
        mCallbacks = callbacks
    }

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

}
