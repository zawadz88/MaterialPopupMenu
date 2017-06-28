package com.github.zawadz88.materialpopupmenu.internal

import android.content.Context
import android.support.annotation.StyleRes
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenu
import com.github.zawadz88.materialpopupmenu.R


/**
 * RecyclerView adapter used for displaying popup menu items grouped in sections.
 * @author Piotr Zawadzki
 */
internal class PopupMenuAdapter(
        context: Context,
        @StyleRes style: Int,
        val sections: List<MaterialPopupMenu.PopupMenuSection>,
        val onItemClickedCallback: (MaterialPopupMenu.PopupMenuItem) -> Unit)
    : SectionedRecyclerViewAdapter<PopupMenuAdapter.SectionHeaderViewHolder, PopupMenuAdapter.ItemViewHolder>() {

    private val contextThemeWrapper: ContextThemeWrapper

    init {
        setHasStableIds(true)
        contextThemeWrapper = ContextThemeWrapper(context, context.theme)
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

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.mpm_popup_menu_item, parent, false)
        return ItemViewHolder(v)
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

    override fun onBindItemViewHolder(holder: ItemViewHolder, section: Int, position: Int) {
        val popupMenuItem = sections[section].items[position]

        holder.label.text = popupMenuItem.label
        if (popupMenuItem.icon != 0) {
            holder.icon.apply {
                visibility = View.VISIBLE
                setImageResource(popupMenuItem.icon)
            }
        } else {
            holder.icon.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            popupMenuItem.callback()
            onItemClickedCallback(popupMenuItem)
        }
    }

    internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_item_label) as TextView

        var icon: ImageView = itemView.findViewById(R.id.mpm_popup_menu_item_icon) as ImageView

    }

    internal class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_section_header_label) as TextView

        var separator: View = itemView.findViewById(R.id.mpm_popup_menu_section_separator)

    }

}
