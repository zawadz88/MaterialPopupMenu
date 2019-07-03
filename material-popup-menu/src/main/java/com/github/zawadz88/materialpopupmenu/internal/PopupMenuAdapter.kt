package com.github.zawadz88.materialpopupmenu.internal

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenu
import com.github.zawadz88.materialpopupmenu.R

/**
 * RecyclerView adapter used for displaying popup menu items grouped in sections.
 *
 * @author Piotr Zawadzki
 */
@SuppressLint("RestrictedApi")
internal class PopupMenuAdapter(
    private val sections: List<MaterialPopupMenu.PopupMenuSection>,
    private val dismissPopupCallback: () -> Unit
) : SectionedRecyclerViewAdapter<PopupMenuAdapter.SectionHeaderViewHolder, PopupMenuAdapter.AbstractItemViewHolder>() {

    init {
        setHasStableIds(false)
    }

    override fun getItemCountForSection(section: Int): Int {
        return sections[section].items.size
    }

    override val sectionCount: Int
        get() = sections.size

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): SectionHeaderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mpm_popup_menu_section_header, parent, false)
        return SectionHeaderViewHolder(v)
    }

    override fun getSectionItemViewType(section: Int, position: Int): Int {
        return when (val popupMenuItem = sections[section].items[position]) {
            is MaterialPopupMenu.PopupMenuCustomItem -> popupMenuItem.layoutResId
            else -> super.getSectionItemViewType(section, position)
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): AbstractItemViewHolder {
        return if (viewType == TYPE_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.mpm_popup_menu_item, parent, false)
            ItemViewHolder(v, dismissPopupCallback)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            CustomItemViewHolder(v, dismissPopupCallback)
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
            if (popupMenuItem.dismissOnSelect) {
                dismissPopupCallback()
            }
        }
    }

    internal abstract class AbstractItemViewHolder(
        itemView: View,
        private val dismissPopupCallback: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        @CallSuper
        open fun bindItem(popupMenuItem: MaterialPopupMenu.AbstractPopupMenuItem) {
            popupMenuItem.viewBoundCallback.dismissPopupAction = dismissPopupCallback
            popupMenuItem.viewBoundCallback.invoke(itemView)
        }
    }

    internal class ItemViewHolder(itemView: View, dismissPopupCallback: () -> Unit) : AbstractItemViewHolder(itemView, dismissPopupCallback) {

        private var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_item_label)

        private var icon: AppCompatImageView = itemView.findViewById(R.id.mpm_popup_menu_item_icon)

        private var nestedIcon: AppCompatImageView = itemView.findViewById(R.id.mpm_popup_menu_item_nested_icon)

        override fun bindItem(popupMenuItem: MaterialPopupMenu.AbstractPopupMenuItem) {
            val castedPopupMenuItem = popupMenuItem as MaterialPopupMenu.PopupMenuItem
            if (castedPopupMenuItem.label != null) {
                label.text = castedPopupMenuItem.label
            } else {
                label.setText(castedPopupMenuItem.labelRes)
            }
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
            nestedIcon.visibility = if (castedPopupMenuItem.hasNestedItems) View.VISIBLE else View.GONE
            super.bindItem(popupMenuItem)
        }
    }

    internal class CustomItemViewHolder(itemView: View, dismissPopupCallback: () -> Unit) : AbstractItemViewHolder(itemView, dismissPopupCallback)

    internal class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var label: TextView = itemView.findViewById(R.id.mpm_popup_menu_section_header_label)

        var separator: View = itemView.findViewById(R.id.mpm_popup_menu_section_separator)
    }
}
