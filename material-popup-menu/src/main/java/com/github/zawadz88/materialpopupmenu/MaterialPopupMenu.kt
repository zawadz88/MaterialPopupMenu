package com.github.zawadz88.materialpopupmenu

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v7.widget.MaterialRecyclerViewPopupWindow
import android.view.View
import com.github.zawadz88.materialpopupmenu.internal.PopupMenuAdapter


/**
 * Holds all the required information for showing a popup menu.
 *
 * @param style Style of the popup menu. See [MaterialPopupMenuBuilder.style]
 * @param dropdownGravity Gravity of the dropdown list. See [MaterialPopupMenuBuilder.dropdownGravity]
 * @param sections a list of sections
 *
 * @author Piotr Zawadzki
 */
class MaterialPopupMenu internal constructor(
        @StyleRes internal val style: Int,
        internal val dropdownGravity: Int,
        internal val sections: List<PopupMenuSection>) {

    private var popupWindow: MaterialRecyclerViewPopupWindow? = null

    /**
     * Shows a popup menu in the UI.
     *
     * This must be called on the UI thread.
     * @param context Context
     * @param anchor view used to anchor the popup
     */
    @UiThread
    fun show(context: Context, anchor: View) {
        val popupWindow = MaterialRecyclerViewPopupWindow(context, dropdownGravity, style)
        val adapter = PopupMenuAdapter(context, style, sections, { popupWindow.dismiss() })

        popupWindow.adapter = adapter
        popupWindow.anchorView = anchor

        popupWindow.show()
        this.popupWindow = popupWindow
    }

    /**
     * Dismisses the popup window.
     */
    @UiThread
    fun dismiss() {
        this.popupWindow?.dismiss()
    }

    internal data class PopupMenuSection(
            val title: String?,
            val items: List<AbstractPopupMenuItem>
    )

    internal data class PopupMenuItem(
            val label: String,
            @ColorInt val labelColor: Int,
            @DrawableRes val icon: Int,
            val iconDrawable: Drawable?,
            @ColorInt val iconColor: Int,
            override val callback: () -> Unit
    ) : AbstractPopupMenuItem(callback)

    internal data class PopupMenuCustomItem(
            @LayoutRes val layoutResId: Int,
            val viewBoundCallback: (View) -> Unit,
            override val callback: () -> Unit
    ) : AbstractPopupMenuItem(callback)


    internal abstract class AbstractPopupMenuItem(
            open val callback: () -> Unit
    )

}
