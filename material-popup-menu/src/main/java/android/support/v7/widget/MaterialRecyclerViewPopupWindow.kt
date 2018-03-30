package android.support.v7.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.annotation.StyleRes
import android.support.v4.widget.PopupWindowCompat
import android.support.v7.view.ContextThemeWrapper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.github.zawadz88.materialpopupmenu.R
import com.github.zawadz88.materialpopupmenu.internal.PopupMenuAdapter
import java.lang.reflect.Method

/**
 * A more Material version of [ListPopupWindow] based on [RecyclerView].
 *
 * Its width is a multiple of 56dp units with a minimum of 112dp and a maximum of 280dp
 * as stated in the <a href="https://material.io/guidelines/components/menus.html#menus-simple-menus">Material documentation</a>
 *
 * @see ListPopupWindow
 */
@SuppressLint("PrivateResource,RestrictedApi")
class MaterialRecyclerViewPopupWindow(
        context: Context,
        private var dropDownGravity: Int,
        @StyleRes defStyleRes: Int) {

    companion object {

        private val TAG = "MaterialRVPopupWindow"

        private var sClipToWindowEnabledMethod: Method? = null
        private var sGetMaxAvailableHeightMethod: Method? = null

        init {
            try {
                sClipToWindowEnabledMethod = PopupWindow::class.java.getDeclaredMethod(
                        "setClipToScreenEnabled", Boolean::class.javaPrimitiveType)
            } catch (e: NoSuchMethodException) {
                Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.")
            }

            try {
                sGetMaxAvailableHeightMethod = PopupWindow::class.java.getDeclaredMethod(
                        "getMaxAvailableHeight", View::class.java, Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            } catch (e: NoSuchMethodException) {
                Log.i(TAG, "Could not find method getMaxAvailableHeight(View, int, boolean)" + " on PopupWindow. Oh well.")
            }

        }
    }

    /**
     * The view that will be used to anchor this popup.
     */
    internal var anchorView: View? = null

    internal var adapter: PopupMenuAdapter? = null
        set(value) {
            setContentWidth(measureIndividualMenuWidth(checkNotNull(value)))
            field = value
        }

    private var dropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT

    private var dropDownVerticalOffset: Int = 0

    private var dropDownHorizontalOffset: Int = 0

    private val tempRect = Rect()

    private val popup: PopupWindow

    private val popupMaxWidth: Int

    private val popupMinWidth: Int

    private val popupWidthUnit: Int

    private val contextThemeWrapper: Context

    init {
        contextThemeWrapper = ContextThemeWrapper(context, null)
        contextThemeWrapper.setTheme(defStyleRes)

        popup = AppCompatPopupWindow(contextThemeWrapper, null, 0, defStyleRes)
        popup.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popup.isFocusable = true

        popupMaxWidth = contextThemeWrapper.resources.getDimensionPixelSize(R.dimen.mpm_popup_menu_max_width)
        popupMinWidth = contextThemeWrapper.resources.getDimensionPixelSize(R.dimen.mpm_popup_menu_min_width)
        popupWidthUnit = contextThemeWrapper.resources.getDimensionPixelSize(R.dimen.mpm_popup_menu_width_unit)

        val a = context.obtainStyledAttributes(null, android.support.v7.appcompat.R.styleable.ListPopupWindow,
                0, defStyleRes)

        dropDownHorizontalOffset = a.getDimensionPixelOffset(
                android.support.v7.appcompat.R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0)
        a.recycle()
    }

    /**
     * Sets the width of the popupMenu window by the size of its content. The final width may be
     * larger to accommodate styled window dressing.

     * @param width Desired width of content in pixels.
     */
    fun setContentWidth(width: Int) {
        val popupBackground = popup.background
        dropDownWidth = if (popupBackground != null) {
            popupBackground.getPadding(tempRect)
            tempRect.left + tempRect.right + width
        } else {
            width
        }
    }

    /**
     * Show the popupMenu list. If the list is already showing, this method
     * will recalculate the popupMenu's size and position.
     */
    fun show() {
        checkNotNull(anchorView, { "Anchor view must be set!" })
        val height = buildDropDown()

        PopupWindowCompat.setWindowLayoutType(popup, WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL)

        val heightSpec = height
        val widthSpec = dropDownWidth
        if (popup.isShowing) {

            popup.isOutsideTouchable = true

            popup.update(anchorView, dropDownHorizontalOffset,
                    dropDownVerticalOffset, widthSpec,
                    if (heightSpec < 0) -1 else heightSpec)
        } else {
            popup.width = widthSpec
            popup.height = heightSpec
            setPopupClipToScreenEnabled(true)

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            popup.isOutsideTouchable = true
            PopupWindowCompat.showAsDropDown(popup, anchorView!!, dropDownHorizontalOffset,
                    dropDownVerticalOffset, dropDownGravity)
        }
    }

    /**
     * Dismiss the popupMenu window.
     */
    fun dismiss() {
        popup.dismiss()
        popup.contentView = null
    }

    /**
     *
     * Builds the popupMenu window's content and returns the height the popupMenu
     * should have.

     * @return the content's height
     */
    private fun buildDropDown(): Int {
        var otherHeights = 0

        val dropDownList = View.inflate(contextThemeWrapper, R.layout.mpm_popup_menu, null) as RecyclerView
        dropDownList.adapter = adapter
        dropDownList.layoutManager = LinearLayoutManager(this.contextThemeWrapper)
        dropDownList.isFocusable = true
        dropDownList.isFocusableInTouchMode = true

        popup.contentView = dropDownList


        // getMaxAvailableHeight() subtracts the padding, so we put it back
        // to get the available height for the whole window.
        val padding: Int
        val background = popup.background
        if (background != null) {
            background.getPadding(tempRect)
            padding = tempRect.top + tempRect.bottom

            // If we don't have an explicit vertical offset, determine one from
            // the window background so that content will line up.
            dropDownVerticalOffset = -tempRect.top
        } else {
            tempRect.setEmpty()
            padding = 0
        }

        // Max height available on the screen for a popupMenu.
        val ignoreBottomDecorations = popup.inputMethodMode == PopupWindow.INPUT_METHOD_NOT_NEEDED
        val maxHeight = getMaxAvailableHeight(anchorView!!, dropDownVerticalOffset,
                ignoreBottomDecorations)

        val listContent = measureHeightOfChildrenCompat(maxHeight - otherHeights)
        if (listContent > 0) {
            val listPadding = dropDownList.paddingTop + dropDownList.paddingBottom
            otherHeights += padding + listPadding
        }

        return listContent + otherHeights
    }

    /**
     * Measures the height of the given range of children (inclusive) and returns the height
     * with this ListView's padding and divider heights included. If maxHeight is provided, the
     * measuring will stop when the current height reaches maxHeight.

     * @param maxHeight                    The maximum height that will be returned (if all the
     * *                                     children don't fit in this value, this value will be
     * *                                     returned).
     * *
     * @return The height of this ListView with the given children.
     *
     * @see DropDownListView.measureHeightOfChildrenCompat
     */
    private fun measureHeightOfChildrenCompat(maxHeight: Int): Int {

        val parent = FrameLayout(contextThemeWrapper)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        // Include the padding of the list
        var returnedHeight = 0

        val count = adapter?.itemCount ?: 0
        for (i in 0 until count) {
            val positionType = adapter!!.getItemViewType(i)

            val vh = adapter!!.createViewHolder(parent, positionType)
            adapter!!.bindViewHolder(vh, i)
            val itemView = vh.itemView

            // Compute child height spec
            val heightMeasureSpec: Int
            var childLp: ViewGroup.LayoutParams? = itemView.layoutParams

            if (childLp == null) {
                childLp = generateDefaultLayoutParams()
                itemView.layoutParams = childLp
            }

            if (childLp.height > 0) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(childLp.height,
                        View.MeasureSpec.EXACTLY)
            } else {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec)

            returnedHeight += itemView.measuredHeight

            if (returnedHeight >= maxHeight) {
                // We went over, figure out which height to return.  If returnedHeight >
                // maxHeight, then the i'th position did not fit completely.
                return maxHeight
            }
        }

        // At this point, we went through the range of children, and they each
        // completely fit, so return the returnedHeight
        return returnedHeight
    }

    private fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setPopupClipToScreenEnabled(clip: Boolean) {
        sClipToWindowEnabledMethod?.let {
            try {
                it.invoke(popup, clip)
            } catch (e: Exception) {
                Log.i(TAG, "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.")
            }
        }
    }

    private fun getMaxAvailableHeight(anchor: View, yOffset: Int, ignoreBottomDecorations: Boolean): Int {
        sGetMaxAvailableHeightMethod?.let {
            try {
                return it.invoke(popup, anchor, yOffset,
                        ignoreBottomDecorations) as Int
            } catch (e: Exception) {
                Log.i(TAG, "Could not call getMaxAvailableHeightMethod(View, int, boolean)" + " on PopupWindow. Using the public version.")
            }
        }
        return popup.getMaxAvailableHeight(anchor, yOffset)
    }

    /**
     * @see android.support.v7.view.menu.MenuPopup.measureIndividualMenuWidth
     */
    private fun measureIndividualMenuWidth(adapter: PopupMenuAdapter): Int {
        adapter.setupIndices()
        val parent = FrameLayout(contextThemeWrapper)
        var menuWidth = popupMinWidth

        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val count = adapter.itemCount
        for (i in 0 until count) {
            val positionType = adapter.getItemViewType(i)

            val vh = adapter.createViewHolder(parent, positionType)
            adapter.bindViewHolder(vh, i)
            val itemView = vh.itemView
            itemView.measure(widthMeasureSpec, heightMeasureSpec)

            val itemWidth = itemView.measuredWidth
            if (itemWidth >= popupMaxWidth) {
                return popupMaxWidth
            } else if (itemWidth > menuWidth) {
                menuWidth = itemWidth
            }
        }

        menuWidth = Math.ceil(menuWidth.toDouble() / popupWidthUnit).toInt() * popupWidthUnit

        return menuWidth
    }

}
