package com.github.zawadz88.materialpopupmenu.sample.persistence

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.github.zawadz88.materialpopupmenu.sample.R

class SelectionRepositoryImpl(private val context: Context) : SelectionRepository {

    override var theme: Int
        get() = prefs.getInt(THEME_KEY, R.style.AppTheme_Light)
        set(value) = prefs.edit(commit = true) { putInt(THEME_KEY, value) }

    override var popupStyle: Int
        get() = prefs.getInt(POPUP_STYLE_KEY, DEFAULT_POPUP_STYLE)
        set(value) = prefs.edit(commit = true) { putInt(POPUP_STYLE_KEY, value) }

    override var dropdownGravities: List<Int>
        get() = prefs.getString(POPUP_DROPDOWN_GRAVITIES_KEY, null)
            ?.split(",")
            ?.filter { it != "" }
            ?.map { it.toInt() }
            ?: emptyList()
        set(value) = prefs.edit(commit = true) { putString(POPUP_DROPDOWN_GRAVITIES_KEY, value.joinToString(separator = ",") { it.toString() }) }

    override var samplePosition: Int
        get() = prefs.getInt(SAMPLE_POSITION_KEY, 0)
        set(value) = prefs.edit(commit = true) { putInt(SAMPLE_POSITION_KEY, value) }

    override var anchorWidthInDp: Int
        get() = prefs.getInt(ANCHOR_WIDTH_KEY, defaultAnchorWidthInDp)
        set(value) = prefs.edit(commit = true) { putInt(ANCHOR_WIDTH_KEY, value) }

    private val defaultAnchorWidthInDp: Int by lazy { context.resources.getInteger(R.integer.showPopupButtonSizeInDp) }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
}

private const val THEME_KEY = "theme"

private const val POPUP_STYLE_KEY = "popupStyle"

private const val POPUP_DROPDOWN_GRAVITIES_KEY = "gravities"

private const val SAMPLE_POSITION_KEY = "samplePosition"

private const val ANCHOR_WIDTH_KEY = "anchorWidth"
