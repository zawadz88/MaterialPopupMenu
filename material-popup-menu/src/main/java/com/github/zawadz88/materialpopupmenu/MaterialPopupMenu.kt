package com.github.zawadz88.materialpopupmenu

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.appcompat.view.ContextThemeWrapper
import com.github.zawadz88.materialpopupmenu.internal.MaterialRecyclerViewPopupWindow
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
class MaterialPopupMenu
internal constructor(
    @StyleRes internal val style: Int,
    internal val dropdownGravity: Int,
    internal val sections: List<PopupMenuSection>,
    internal val fixedContentWidthInPx: Int,
    internal val dropDownVerticalOffset: Int?,
    internal val dropDownHorizontalOffset: Int?
) {

    private var popupWindow: MaterialRecyclerViewPopupWindow? = null

    private var dismissListener: (() -> Unit)? = null

    /**
     * Shows a popup menu in the UI.
     *
     * This must be called on the UI thread.
     * @param context Context
     * @param anchor view used to anchor the popup
     */
    @UiThread
    fun show(context: Context, anchor: View) {
        val style = resolvePopupStyle(context)
        val styledContext = ContextThemeWrapper(context, style)
        val popupWindow = MaterialRecyclerViewPopupWindow(
            context = styledContext,
            dropDownGravity = dropdownGravity,
            fixedContentWidthInPx = fixedContentWidthInPx,
            dropDownVerticalOffset = dropDownVerticalOffset,
            dropDownHorizontalOffset = dropDownHorizontalOffset
        )
        val adapter = PopupMenuAdapter(sections) { popupWindow.dismiss() }

        popupWindow.adapter = adapter
        popupWindow.anchorView = anchor

        popupWindow.show()
        this.popupWindow = popupWindow
        setOnDismissListener(this.dismissListener)
    }

    /**
     * Dismisses the popup window.
     */
    @UiThread
    fun dismiss() {
        this.popupWindow?.dismiss()
    }

    /**
     * Sets a listener that is called when this popup window is dismissed.
     *
     * @param listener Listener that is called when this popup window is dismissed.
     */
    fun setOnDismissListener(listener: (() -> Unit)?) {
        this.dismissListener = listener
        this.popupWindow?.setOnDismissListener(listener)
    }

    private fun resolvePopupStyle(context: Context): Int {
        if (style != 0) {
            return style
        }

        val a = context.obtainStyledAttributes(intArrayOf(R.attr.materialPopupMenuStyle))
        val resolvedStyle = a.getResourceId(0, R.style.Widget_MPM_Menu)
        a.recycle()

        return resolvedStyle
    }

    internal data class PopupMenuSection(
        val title: CharSequence?,
        val items: List<AbstractPopupMenuItem>
    )

    internal data class PopupMenuItem(
        val label: CharSequence?,
        @StringRes val labelRes: Int,
        @ColorInt val labelColor: Int,
        @DrawableRes val icon: Int,
        val iconDrawable: Drawable?,
        @ColorInt val iconColor: Int,
        val hasNestedItems: Boolean,
        override val viewBoundCallback: ViewBoundCallback,
        override val callback: () -> Unit,
        override val dismissOnSelect: Boolean
    ) : AbstractPopupMenuItem(callback, dismissOnSelect, viewBoundCallback)

    internal data class PopupMenuCustomItem(
        @LayoutRes val layoutResId: Int,
        override val viewBoundCallback: ViewBoundCallback,
        override val callback: () -> Unit,
        override val dismissOnSelect: Boolean
    ) : AbstractPopupMenuItem(callback, dismissOnSelect, viewBoundCallback)

    internal abstract class AbstractPopupMenuItem(
        open val callback: () -> Unit,
        open val dismissOnSelect: Boolean,
        open val viewBoundCallback: ViewBoundCallback
    )
}
