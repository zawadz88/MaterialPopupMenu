package com.github.zawadz88.materialpopupmenu

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v7.widget.MaterialRecyclerViewPopupWindow
import android.view.View
import com.github.zawadz88.materialpopupmenu.internal.PopupMenuAdapter


/**
 *
 *
 * @author Piotr Zawadzki
 */
class MaterialPopupMenu internal constructor(
        @StyleRes val style: Int,
        val sections: List<PopupMenuSection>) {

    fun show(context: Context, anchor: View) {
        val popupWindow = MaterialRecyclerViewPopupWindow(context, style)
        val adapter = PopupMenuAdapter(context, sections, { popupWindow.dismiss() })

        popupWindow.adapter = adapter
        popupWindow.anchorView = anchor

        popupWindow.show()
    }

    internal data class PopupMenuSection(
            val title: String?,
            val items: List<PopupMenuItem>
    )

    internal data class PopupMenuItem(
            val label: String,
            @DrawableRes val icon: Int,
            val callback: () -> Unit
    )

}
