package com.github.zawadz88.materialpopupmenu.internal

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenu
import com.github.zawadz88.materialpopupmenu.R


/**
 * @author Piotr Zawadzki
 */
internal class PopupMenuAdapter(
        val context: Context,
        val sections: List<MaterialPopupMenu.PopupMenuSection>,
        val onItemClickedCallback: (MaterialPopupMenu.PopupMenuItem) -> Unit)
    : SectionedRecyclerViewAdapter<PopupMenuAdapter.SectionHeaderViewHolder, PopupMenuAdapter.ItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemCountForSection(sectionPosition: Int): Int {
        return sections[sectionPosition].items.size
    }

    override fun getSectionCount(): Int {
        return sections.size
    }

    override fun hasHeaderInSection(sectionPosition: Int): Boolean {
        return sections[sectionPosition].title != null
    }

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup?, viewType: Int): SectionHeaderViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.mpm_popup_menu_section_header, parent, false)
        return SectionHeaderViewHolder(v)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.mpm_popup_menu_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindSectionHeaderViewHolder(holder: SectionHeaderViewHolder, sectionPosition: Int) {
        holder.label.text = sections[sectionPosition].title
    }

    override fun onBindItemViewHolder(holder: ItemViewHolder, sectionPosition: Int, position: Int) {
        val popupMenuItem = sections[sectionPosition].items[position]

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

    }

}
