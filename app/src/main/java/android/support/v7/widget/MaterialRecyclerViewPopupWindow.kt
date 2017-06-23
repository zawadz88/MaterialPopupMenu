/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v7.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.StyleRes
import android.support.v4.widget.PopupWindowCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.github.zawadz88.materialpopupmenu.R
import com.github.zawadz88.materialpopupmenu.internal.PopupMenuAdapter
import java.lang.reflect.Method

/**
 *
 * @see ListPopupWindow
 */
class MaterialRecyclerViewPopupWindow(
        private val context: Context,
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
            setContentWidth(measureIndividualMenuWidth(checkNotNull(value), context, popupMaxWidth))
            field = value
        }

    private var dropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT

    private var dropDownVerticalOffset: Int = 0

    private val tempRect = Rect()

    private val popup: PopupWindow

    private val popupMaxWidth: Int

    init {
        popup = AppCompatPopupWindow(context, null, 0, defStyleRes)
        popup.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popup.isFocusable = true

        popupMaxWidth = context.resources.displayMetrics.widthPixels * 2 / 3
    }

    /**
     * Sets a drawable to be the background for the popup window.

     * @param d A drawable to set as the background.
     */
    fun setBackgroundDrawable(d: Drawable?) {
        popup.setBackgroundDrawable(d)
    }

    /**
     * Set an animation style to use when the popupMenu window is shown or dismissed.

     * @param animationStyle Animation style to use.
     */
    fun setAnimationStyle(@StyleRes animationStyle: Int) {
        popup.animationStyle = animationStyle
    }

    /**
     * Sets the width of the popupMenu window by the size of its content. The final width may be
     * larger to accommodate styled window dressing.

     * @param width Desired width of content in pixels.
     */
    fun setContentWidth(width: Int) {
        val popupBackground = popup.background
        if (popupBackground != null) {
            popupBackground.getPadding(tempRect)
            dropDownWidth = tempRect.left + tempRect.right + width
        } else {
            dropDownWidth = width
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
        if (popup.isShowing) {
            val widthSpec: Int
            if (dropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                widthSpec = -1
            } else if (dropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                widthSpec = anchorView!!.width
            } else {
                widthSpec = dropDownWidth
            }


            popup.isOutsideTouchable = true

            popup.update(anchorView, 0,
                    dropDownVerticalOffset, if (widthSpec < 0) -1 else widthSpec,
                    if (heightSpec < 0) -1 else heightSpec)
        } else {
            val widthSpec: Int
            if (dropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                widthSpec = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                if (dropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    widthSpec = anchorView!!.width
                } else {
                    widthSpec = dropDownWidth
                }
            }

            popup.width = widthSpec
            popup.height = heightSpec
            setPopupClipToScreenEnabled(true)

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            popup.isOutsideTouchable = true
            PopupWindowCompat.showAsDropDown(popup, anchorView, 0,
                    dropDownVerticalOffset, Gravity.NO_GRAVITY)

            PopupWindowCompat.showAsDropDown(popup, anchorView, 0,
                    dropDownVerticalOffset, Gravity.NO_GRAVITY)
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
     * Set a listener to receive a callback when the popupMenu is dismissed.

     * @param listener Listener that will be notified when the popupMenu is dismissed.
     */
    fun setOnDismissListener(listener: PopupWindow.OnDismissListener?) {
        popup.setOnDismissListener(listener)
    }

    /**
     * @return `true` if the popupMenu is currently showing, `false` otherwise.
     */
    val isShowing: Boolean
        get() = popup.isShowing


    /**
     *
     * Builds the popupMenu window's content and returns the height the popupMenu
     * should have. Returns -1 when the content already exists.

     * @return the content's height or -1 if content already exists
     */
    private fun buildDropDown(): Int {
        var otherHeights = 0

        val dropDownList = View.inflate(context, R.layout.mpm_popup_menu, null) as RecyclerView
        dropDownList.adapter = adapter
        dropDownList.layoutManager = LinearLayoutManager(this.context)
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

        // Add padding only if the list has items in it, that way we don't show
        // the popup if it is not needed.
        val listContent = measureHeightOfChildrenCompat(maxHeight - otherHeights, -1, dropDownList)
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
     * @param disallowPartialChildPosition In general, whether the returned height should only
     * *                                     contain entire children. This is more powerful--it is
     * *                                     the first inclusive position at which partial
     * *                                     children will not be allowed. Example: it looks nice
     * *                                     to have at least 3 completely visible children, and
     * *                                     in portrait this will most likely fit; but in
     * *                                     landscape there could be times when even 2 children
     * *                                     can not be completely shown, so a value of 2
     * *                                     (remember, inclusive) would be good (assuming
     * *                                     startPosition is 0).
     * *
     * @return The height of this ListView with the given children.
     */
    private fun measureHeightOfChildrenCompat(maxHeight: Int,
                                              disallowPartialChildPosition: Int,
                                              dropDownList: RecyclerView): Int {

        val paddingTop = dropDownList.paddingTop
        val paddingBottom = dropDownList.paddingBottom

        // Include the padding of the list
        var returnedHeight = paddingTop + paddingBottom

        // The previous height value that was less than maxHeight and contained
        // no partial children
        var prevHeightWithoutPartialChild = 0

        val count = adapter?.itemCount ?: 0
        for (i in 0..count - 1) {
            //TODO calculate this instead
            val itemHeight = context.resources.getDimensionPixelSize(R.dimen.mpm_popup_menu_item_height)
            returnedHeight += itemHeight

            if (returnedHeight >= maxHeight) {
                // We went over, figure out which height to return.  If returnedHeight >
                // maxHeight, then the i'th position did not fit completely.
                return if (disallowPartialChildPosition in 0..(i - 1) // We've past the min pos
                        && prevHeightWithoutPartialChild > 0 // We have a prev height
                        && returnedHeight != maxHeight // i'th child did not fit completely
                               )
                    prevHeightWithoutPartialChild
                else
                    maxHeight
            }

            if (disallowPartialChildPosition in 0..i) {
                prevHeightWithoutPartialChild = returnedHeight
            }
        }

        // At this point, we went through the range of children, and they each
        // completely fit, so return the returnedHeight
        return returnedHeight
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
    private fun measureIndividualMenuWidth(adapter: PopupMenuAdapter, context: Context, maxAllowedWidth: Int): Int {
        adapter.setupIndices()
        val parent = FrameLayout(context)
        // Menus don't tend to be long, so this is more sane than it looks.
        var maxWidth = 0

        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val count = adapter.itemCount
        for (i in 0..count - 1) {
            val positionType = adapter.getItemViewType(i)

            val vh = adapter.createViewHolder(parent, positionType)
            adapter.bindViewHolder(vh, i)
            val itemView = vh.itemView
            itemView.measure(widthMeasureSpec, heightMeasureSpec)

            val itemWidth = itemView.measuredWidth
            if (itemWidth >= maxAllowedWidth) {
                return maxAllowedWidth
            } else if (itemWidth > maxWidth) {
                maxWidth = itemWidth
            }
        }

        return maxWidth
    }

}
