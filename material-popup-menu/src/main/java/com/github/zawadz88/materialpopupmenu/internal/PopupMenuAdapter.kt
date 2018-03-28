package com.github.zawadz88.materialpopupmenu.internal

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.StyleRes
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenu
import com.github.zawadz88.materialpopupmenu.R


/**
 * RecyclerView adapter used for displaying popup menu items grouped in sections.
 *
 * @author Piotr Zawadzki
 */
@SuppressLint("RestrictedApi")
internal class PopupMenuAdapter(
        context: Context,
        @StyleRes style: Int,
        private val sections: List<MaterialPopupMenu.PopupMenuSection>,
        private val onItemClickedCallback: (MaterialPopupMenu.AbstractPopupMenuItem) -> Unit)
    : SectionedRecyclerViewAdapter<PopupMenuAdapter.SectionHeaderViewHolder, PopupMenuAdapter.AbstractItemViewHolder>() {

    private val contextThemeWrapper: ContextThemeWrapper

    init {
        setHasStableIds(false)
        contextThemeWrapper = ContextThemeWrapper(context, null)
        contextThemeWrapper.setTheme(style)
    }

    override fun getItemCountForSection(section: Int): Int {
        return sections[section].items.size
    }

    override val sectionCount: Int
        get() = sections.size

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): SectionHeaderViewHolder {
        val v = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.mpm_popup_menu_section_header, parent, false)
        return SectionHeaderViewHolder(v)
    }

    override fun getSectionItemViewType(section: Int, position: Int): Int {
        val popupMenuItem = sections[section].items[position]
        return when (popupMenuItem) {
            is MaterialPopupMenu.PopupMenuCustomItem -> popupMenuItem.layoutResId
            else -> super.getSectionItemViewType(section, position)
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): AbstractItemViewHolder {
        return if (viewType == TYPE_ITEM) {
            val v = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.mpm_popup_menu_item, parent, false)
            ItemViewHolder(v)
        } else {
            val v = LayoutInflater.from(contextThemeWrapper).inflate(viewType, parent, false)
            CustomItemViewHolder(v)
        }
    }

    override fun onBindSectionHeaderViewHolder(holder: SectionHeaderViewHolder, sectionPosition: Int) {
        val title = sections[sectionPosition].title
        if (title != null) {
            holder.label.visibility = View.VISIBLE
            holder.label.text = title
        } else {
            holder.label.visibility = View.GONE
        }

        holder.separator.visibility = if (sectionPosition == 0) View.GONE else View.VISIBLE
    }

    override fun onBindItemViewHolder(holder: AbstractItemViewHolder, section: Int, position: Int) {
        val popupMenuItem = sections[section].items[position]
        holder.bindItem(popupMenuItem)
        holder.itemView.setOnClickListener {
            popupMenuItem.callback()
            onItemClickedCallback(popupMenuItem)
        }
    }

    internal abstract class AbstractItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bindItem(popupMenuItem: MaterialPopupMenu.AbstractPopupMenuItem)
    }

    internal class ItemViewHolder(itemView: View) : AbstractItemViewHolder(itemView) {

        var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_item_label)

        var icon: AppCompatImageView = itemView.findViewById(R.id.mpm_popup_menu_item_icon)

        override fun bindItem(popupMenuItem: MaterialPopupMenu.AbstractPopupMenuItem) {
            val castedPopupMenuItem = popupMenuItem as MaterialPopupMenu.PopupMenuItem
            label.text = castedPopupMenuItem.label
            if (castedPopupMenuItem.icon != 0 || castedPopupMenuItem.iconDrawable != null) {
                icon.apply {
                    visibility = View.VISIBLE
                    setImageResource(castedPopupMenuItem.icon)
                    castedPopupMenuItem.iconDrawable?.let { setImageDrawable(it) }
                    if (castedPopupMenuItem.iconColor != 0) {
                        supportImageTintList = ColorStateList.valueOf(castedPopupMenuItem.iconColor)
                    }
                }
            } else {
                icon.visibility = View.GONE
            }
            if (castedPopupMenuItem.labelColor != 0) {
                label.setTextColor(castedPopupMenuItem.labelColor)
            }
        }

    }

    internal class CustomItemViewHolder(itemView: View) : AbstractItemViewHolder(itemView) {

        override fun bindItem(popupMenuItem: MaterialPopupMenu.AbstractPopupMenuItem) {
            val popupMenuCustomItem = popupMenuItem as MaterialPopupMenu.PopupMenuCustomItem
            popupMenuCustomItem.viewBoundCallback.invoke(itemView)
        }
    }

    internal class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_section_header_label)

        var separator: View = itemView.findViewById(R.id.mpm_popup_menu_section_separator)

    }

}
