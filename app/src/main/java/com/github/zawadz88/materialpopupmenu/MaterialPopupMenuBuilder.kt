package com.github.zawadz88.materialpopupmenu

/**
 *
 *
 * @author Piotr Zawadzki
 */
@PopupMenuMarker
class MaterialPopupMenuBuilder {

    var style: Int = com.github.zawadz88.materialpopupmenu.R.style.MaterialPopupMenuStyle

    internal val sectionHolderList = arrayListOf<SectionHolder>()

    fun section(init: SectionHolder.() -> Unit) {
        val section = SectionHolder()
        section.init()
        sectionHolderList.add(section)
    }

    fun build(): MaterialPopupMenu {
        check(sectionHolderList.isNotEmpty(), { "Popup menu sections cannot be empty!" })

        val sections = sectionHolderList.map { it.convertToPopupMenuSection() }

        return MaterialPopupMenu(style, sections)
    }

    override fun toString(): String {
        return "MaterialPopupMenuBuilder(style=$style, sectionHolderList=$sectionHolderList)"
    }

    @PopupMenuMarker
    class SectionHolder {

        var title: String? = null

        internal val itemsHolderList = arrayListOf<ItemHolder>()

        fun item(init: ItemHolder.() -> Unit) {
            val item = ItemHolder()
            item.init()
            itemsHolderList.add(item)
        }

        override fun toString(): String {
            return "SectionHolder(title=$title, itemsHolderList=$itemsHolderList)"
        }

        internal fun convertToPopupMenuSection(): MaterialPopupMenu.PopupMenuSection {
            check(itemsHolderList.isNotEmpty(), { "Section '$this' has no items!" })
            return MaterialPopupMenu.PopupMenuSection(title, itemsHolderList.map { it.convertToPopupMenuItem() })
        }

    }

    @PopupMenuMarker
    class ItemHolder {

        var label: String? = null

        var icon: Int = 0

        var callback: () -> Unit = {}

        override fun toString(): String {
            return "ItemHolder(label=$label, icon=$icon, callback=$callback)"
        }

        internal fun convertToPopupMenuItem(): MaterialPopupMenu.PopupMenuItem {
            return MaterialPopupMenu.PopupMenuItem(
                    checkNotNull(label, { "Item '$this' does not have a label" }),
                    icon,
                    callback)
        }

    }

}

fun popupMenuBuilder(init: MaterialPopupMenuBuilder.() -> Unit): MaterialPopupMenuBuilder {
    val popupMenu = MaterialPopupMenuBuilder()
    popupMenu.init()
    return popupMenu
}

fun popupMenu(init: MaterialPopupMenuBuilder.() -> Unit): MaterialPopupMenu {
    return popupMenuBuilder(init).build()
}

@DslMarker
annotation class PopupMenuMarker
