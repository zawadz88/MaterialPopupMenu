package com.github.zawadz88.materialpopupmenu.sample.persistence

import androidx.annotation.StyleRes

interface SelectionRepository {

    @get:StyleRes
    var theme: Int

    @get:StyleRes
    var popupStyle: Int

    var dropdownGravities: List<Int>

    var samplePosition: Int

    var anchorWidthInDp: Int
}

const val DEFAULT_POPUP_STYLE = -1
