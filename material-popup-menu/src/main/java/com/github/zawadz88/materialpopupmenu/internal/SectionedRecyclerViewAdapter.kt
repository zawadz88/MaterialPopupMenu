package com.github.zawadz88.materialpopupmenu.internal

/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * An extension to RecyclerView.Adapter to provide sections with headers to a
 * RecyclerView. Each section can have an arbitrary number of items.
 *
 *
 * Modified version of [SectionedRecyclerView](https://github.com/truizlop/SectionedRecyclerView) adapter without the footers
 * and converted to Kotlin.
 *
 * @param H  Class extending RecyclerView.ViewHolder to hold and bind the header view
 * @param VH Class extending RecyclerView.ViewHolder to hold and bind the items view
 * */
internal abstract class SectionedRecyclerViewAdapter<H : RecyclerView.ViewHolder, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sectionForPosition: IntArray? = null
    private var positionWithinSection: IntArray? = null
    private var isHeader: BooleanArray? = null
    private var count = 0

    /**
     * Returns the sum of number of items for each section plus headers and footers if they
     * are provided.
     */
    override fun getItemCount(): Int {
        return count
    }

    fun setupIndices() {
        count = countItems()
        allocateAuxiliaryArrays(count)
        precomputeIndices()
    }

    private fun countItems(): Int {
        val sections = sectionCount

        return (0 until sections).sumBy { getItemCountForSection(it) + 1 }
    }

    private fun precomputeIndices() {
        val sections = sectionCount
        var index = 0

        for (i in 0 until sections) {
            setPrecomputedItem(index, true, i, 0)
            index++


            for (j in 0 until getItemCountForSection(i)) {
                setPrecomputedItem(index, false, i, j)
                index++
            }
        }
    }

    private fun allocateAuxiliaryArrays(count: Int) {
        sectionForPosition = IntArray(count)
        positionWithinSection = IntArray(count)
        isHeader = BooleanArray(count)
    }

    private fun setPrecomputedItem(index: Int, isHeader: Boolean, section: Int, position: Int) {
        this.isHeader?.set(index, isHeader)
        sectionForPosition?.set(index, section)
        positionWithinSection?.set(index, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isSectionHeaderViewType(viewType)) {
            onCreateSectionHeaderViewHolder(parent, viewType)
        } else {
            onCreateItemViewHolder(parent, viewType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = sectionForPosition!![position]
        val index = positionWithinSection!![position]
        if (isSectionHeaderPosition(position)) {
            onBindSectionHeaderViewHolder(holder as H, section)
        } else {
            onBindItemViewHolder(holder as VH, section, index)
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (sectionForPosition == null) {
            setupIndices()
        }

        val section = sectionForPosition!![position]
        val index = positionWithinSection!![position]

        return if (isSectionHeaderPosition(position)) {
            getSectionHeaderViewType(section)
        } else {
            getSectionItemViewType(section, index)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun getSectionHeaderViewType(section: Int): Int {
        return TYPE_SECTION_HEADER
    }

    @Suppress("UNUSED_PARAMETER")
    protected open fun getSectionItemViewType(section: Int, position: Int): Int {
        return TYPE_ITEM
    }

    /**
     * Returns true if the argument position corresponds to a header
     */
    private fun isSectionHeaderPosition(position: Int): Boolean {
        if (isHeader == null) {
            setupIndices()
        }
        return isHeader!![position]
    }

    private fun isSectionHeaderViewType(viewType: Int): Boolean {
        return viewType == TYPE_SECTION_HEADER
    }

    /**
     * Returns the number of sections in the RecyclerView
     */
    protected abstract val sectionCount: Int

    /**
     * Returns the number of items for a given section
     */
    protected abstract fun getItemCountForSection(section: Int): Int

    /**
     * Creates a ViewHolder of class H for a Header
     */
    protected abstract fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): H

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * Binds data to the header view of a given section
     */
    protected abstract fun onBindSectionHeaderViewHolder(holder: H, sectionPosition: Int)

    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract fun onBindItemViewHolder(holder: VH, section: Int, position: Int)

    companion object {

        internal const val TYPE_SECTION_HEADER = -1
        internal const val TYPE_ITEM = -2
    }
}
