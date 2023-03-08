package com.tiagosantos.common.ui.extension

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.DimenRes
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

fun View.showWhenClicked() {
    setOnClickListener { this.visibility = View.VISIBLE }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hideWhenClicked() {
    setOnClickListener { this.visibility = View.GONE }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.clickAndFocus() {
    performClick(); requestFocus()
}

fun View.invisible() {
    setOnClickListener { this.visibility = View.INVISIBLE }
}

fun View.showAndBringToFront() {
    this.visibility = View.VISIBLE.apply { bringToFront() }
}

fun EditText.setEmptyText() {
    this.setText(EMPTY_STRING)
}

fun TextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

fun View.px(@DimenRes rid: Int): Int {
    return this.context.resources.getDimensionPixelOffset(rid)
}

val SearchView?.getEditTextSearchView get() =
    this?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

val ViewGroup.inflater: LayoutInflater get() = LayoutInflater.from(context)


fun ViewGroup.getFirstChildOrNull(): View? {
    if (childCount == 0) return null
    return getChildAt(0)
}

fun ViewGroup.getLastChildOrNull(): View? {
    if (childCount == 0) return null
    return getChildAt(childCount - 1)
}

/**
 * Get views by tag for ViewGroup.
 */
fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}


/**
 * Remove views by tag ViewGroup.
 */
fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}

/**
 * get All the Children's as Iterator
 */
fun ViewGroup.children() = object : Iterator<View> {
    var index = 0
    override fun hasNext(): Boolean {
        return index < childCount
    }

    override fun next(): View {
        return getChildAt(index++)
    }
}

/**
 * Shows all child views
 */
fun ViewGroup.showAll() {
    eachChild {
        it.show()
    }
}

/**
 * Hides all child views
 */
fun ViewGroup.hideAll() {
    eachChild {
        it.hide()
    }
}

/** Applies given func to all child views **/
inline fun ViewGroup.eachChild(func: (view: View) -> Unit) {
    for (i in 0 until childCount) {
        func(getChildAt(i))
    }
}

fun MaterialCardView.setFrameOnClick(
    cardList: List<MaterialCardView>,
    setChecks: (List<MaterialCardView>, MaterialCardView) -> Unit
) {
    this.setOnClickListener {
        setChecks(cardList,this)
    }
}

val more : (String, Int) -> String = { str, int -> str + int }
