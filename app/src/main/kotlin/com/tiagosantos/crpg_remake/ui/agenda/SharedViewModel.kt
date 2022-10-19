package com.tiagosantos.crpg_remake.ui.agenda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

class SharedViewModel(
    application: Application
) : AndroidViewModel(application) {
    var selectedDate = EMPTY_STRING
}
