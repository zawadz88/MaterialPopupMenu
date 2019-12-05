/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zawadz88.materialpopupmenu.sample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.Callback

import java.util.ArrayList

/**
 * A [ConstraintLayout] whose children can be dragged.
 * Copied & Kotlinized from https://github.com/material-components/material-components-android/blob/3c5f9f6cb0b3847fbb4e1b6b9fbad077f80b0d05/catalog/java/io/material/catalog/draggable/DraggableCoordinatorLayout.java
 */
class DraggableConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    var viewDragListener: ViewDragListener? = null

    private val viewDragHelper: ViewDragHelper

    private val draggableChildren = ArrayList<View>()

    private val dragCallback = object : Callback() {

        override fun tryCaptureView(view: View, i: Int): Boolean = view.visibility == View.VISIBLE && viewIsDraggableChild(view)

        override fun onViewCaptured(view: View, i: Int) {
            viewDragListener?.onViewCaptured(view, i)
        }

        override fun onViewReleased(view: View, v: Float, v1: Float) {
            viewDragListener?.onViewReleased(view, v, v1)
        }

        override fun getViewHorizontalDragRange(view: View): Int = view.width

        override fun getViewVerticalDragRange(view: View): Int = view.height

        override fun clampViewPositionHorizontal(view: View, left: Int, dx: Int): Int = left

        override fun clampViewPositionVertical(view: View, top: Int, dy: Int): Int = top
    }

    init {
        viewDragHelper = ViewDragHelper.create(this, dragCallback)
    }

    fun addDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.add(child)
    }

    fun removeDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.remove(child)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(ev)
        return super.onTouchEvent(ev)
    }

    private fun viewIsDraggableChild(view: View): Boolean = draggableChildren.isEmpty() || draggableChildren.contains(view)

    /**
     * A listener to use when a child view is being dragged
     */
    interface ViewDragListener {
        fun onViewCaptured(view: View, i: Int)

        fun onViewReleased(view: View, v: Float, v1: Float)
    }
}
