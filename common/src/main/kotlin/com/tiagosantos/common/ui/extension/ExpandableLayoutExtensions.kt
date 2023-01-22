package com.tiagosantos.common.ui.extension

import android.widget.LinearLayout
import com.skydoves.expandablelayout.ExpandableLayout

fun List<ExpandableLayout>.setLayoutClickListeners(parentLayout: LinearLayout) {
    parentLayout.setOnClickListener { forEach { it.performClick() } }
}

fun List<ExpandableLayout>.setExpandablesClickListeners() {
    forEach { it -> it.parentLayout.setOnClickListener { it.performClick() } }
}

fun List<ExpandableLayout>.toggleLayout(parentLayout: LinearLayout) = parentLayout.setOnClickListener {
    forEach { it.performClick() }
}

