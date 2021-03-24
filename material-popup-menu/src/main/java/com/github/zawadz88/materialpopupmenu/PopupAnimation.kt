package com.github.zawadz88.materialpopupmenu

import android.view.View
import android.widget.PopupWindow

interface PopupAnimation {

    /* view is not yet layed out */
    fun onPrepare(popup: PopupWindow) {
        popup.contentView.visibility = View.INVISIBLE
    }

    /* view is layed out - animate it */
    fun onShow(popup: PopupWindow)

    /* animate the view dismissal */
    fun onHide(popup: PopupWindow, onHidden: (() -> Unit))
}