package com.tiagosantos.crpg_remake.ui.agenda.timeline.model

import android.os.Parcelable
import com.tiagosantos.crpg_remake.ui.agenda.timeline.model.OrderStatus
import kotlinx.android.parcel.Parcelize

/**
 * Created by Vipul Asri on 05-12-2015.
 */
@Parcelize
class TimeLineModel(
        var message: String,
        var date: String,
        var status: OrderStatus
) : Parcelable
