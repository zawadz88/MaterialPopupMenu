package com.github.zawadz88.materialpopupmenu

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.View

/**
 * Builder for creating a [MaterialPopupMenu].
 *
 * The [MaterialPopupMenu] must have at least one section.
 * All sections must also have at least one item and each item must have a non-null label set.
 *
 * @author Piotr Zawadzki
 */
@PopupMenuMarker
class MaterialPopupMenuBuilder {

    /**
     * Style of the popup menu. Default is [R.style.Widget_MPM_Menu].
     *
     * For dark themes you should use [R.style.Widget_MPM_Menu_Dark].
     *
     * You can also provide your own style, however make sure that all of the attributes
     * that are declared in [R.style.Widget_MPM_Menu] are also declared in your style.
     */
    var style: Int = R.style.Widget_MPM_Menu

    /**
     * Gravity of the dropdown list. This is commonly used to
     * set gravity to START or END for alignment with the anchor.
     */
    var dropdownGravity: Int = Gravity.NO_GRAVITY

    private val sectionHolderList = arrayListOf<SectionHolder>()

    /**
     * Adds a new section to the popup menu.
     *
     * Sections are separated with a divider from each other and must contain at least one item.
     * Section titles are optional.
     * @param init block containing section definition
     */
    fun section(init: SectionHolder.() -> Unit) {
        val section = SectionHolder()
        section.init()
        sectionHolderList.add(section)
    }

    /**
     * Creates a [MaterialPopupMenu] with the already configured params.
     *
     * This might throw [IllegalStateException] if it wasn't configured properly
     * - see class description for validation details.
     */
    fun build(): MaterialPopupMenu {
        check(sectionHolderList.isNotEmpty(), { "Popup menu sections cannot be empty!" })

        val sections = sectionHolderList.map { it.convertToPopupMenuSection() }

        return MaterialPopupMenu(style, dropdownGravity, sections)
    }

    /**
     * Holds section info for the builder. This gets converted to [MaterialPopupMenu.PopupMenuSection].
     */
    @PopupMenuMarker
    class SectionHolder {

        /**
         * Optional section holder. *null* by default.
         * If the title is not *null* it will be displayed in the menu.
         */
        var title: String? = null

        private val itemsHolderList = arrayListOf<AbstractItemHolder>()

        /**
         * Adds an item to the section.
         * @param init block containing item definition
         */
        fun item(init: ItemHolder.() -> Unit) {
            val item = ItemHolder()
            item.init()
            itemsHolderList.add(item)
        }

        /**
         * Adds a custom item to the section.
         * @param init block containing custom item definition
         */
        fun customItem(init: CustomItemHolder.() -> Unit) {
            val item = CustomItemHolder()
            item.init()
            itemsHolderList.add(item)
        }

        override fun toString(): String {
            return "SectionHolder(title=$title, itemsHolderList=$itemsHolderList)"
        }

        internal fun convertToPopupMenuSection(): MaterialPopupMenu.PopupMenuSection {
            check(itemsHolderList.isNotEmpty(), { "Section '$this' has no items!" })
            return MaterialPopupMenu.PopupMenuSection(
                    title = title,
                    items = itemsHolderList.map { it.convertToPopupMenuItem() }
            )
        }

    }

    /**
     * Holds section item info for the builder. This gets converted to [MaterialPopupMenu.PopupMenuItem].
     */
    @PopupMenuMarker
    class ItemHolder : AbstractItemHolder() {

        /**
         * Item label. This is a required field and must not be *null*.
         */
        var label: String? = null

        /**
         * Optional text color of the label. If not set or 0 the default color will be used.
         */
        @ColorInt
        var labelColor: Int = 0

        /**
         * Optional icon to be displayed together with the label.
         *
         * This must be a valid drawable resource ID if set.
         * *0* Means that no icon should be displayed.
         *
         * Alternatively, you can set the drawable using [iconDrawable].
         *
         * If both [icon] and [iconDrawable] are set [iconDrawable] will be used.
         */
        @DrawableRes
        var icon: Int = 0

        /**
         * Optional icon to be displayed together with the label.
         *
         * *null* Means that no icon should be displayed.
         *
         * Alternatively, you can set the drawable using [icon].
         *
         * If both [icon] and [iconDrawable] are set [iconDrawable] will be used.
         */
        var iconDrawable: Drawable? = null

        /**
         * Optional icon tint color.
         *
         * This must be a valid color Int if set.
         * *0* Means that default tinting will be applied.
         */
        @ColorInt
        var iconColor: Int = 0

        override fun toString(): String {
            return "ItemHolder(label=$label, labelColor=$labelColor, icon=$icon, iconDrawable=$iconDrawable, iconColor=$iconColor, callback=$callback)"
        }

        override fun convertToPopupMenuItem(): MaterialPopupMenu.PopupMenuItem {
            return MaterialPopupMenu.PopupMenuItem(
                    label = checkNotNull(label, { "Item '$this' does not have a label" }),
                    labelColor = labelColor,
                    icon = icon,
                    iconDrawable = iconDrawable,
                    iconColor = iconColor,
                    callback = callback)
        }

    }

    /**
     * Holds section custom item info for the builder. This gets converted to [MaterialPopupMenu.PopupMenuCustomItem].
     */
    @PopupMenuMarker
    class CustomItemHolder : AbstractItemHolder() {

        /**
         * Layout ID of the view to be used for this item.
         */
        @LayoutRes
        var layoutResId: Int = 0

        /**
         * Callback to be invoked once the custom item view gets created and bound.
         * It is to be used when some views inside need to be updated once inflated.
         */
        var viewBoundCallback: (View) -> Unit = {}

        override fun toString(): String {
            return "CustomItemHolder(layoutResId=$layoutResId, viewBoundCallback=$viewBoundCallback, callback=$callback)"
        }

        override fun convertToPopupMenuItem(): MaterialPopupMenu.PopupMenuCustomItem {
            check(layoutResId != 0, { "Layout resource ID must be set for a custom item!" })
            return MaterialPopupMenu.PopupMenuCustomItem(
                    layoutResId = layoutResId,
                    viewBoundCallback = viewBoundCallback,
                    callback = callback)
        }

    }

    @PopupMenuMarker
    abstract class AbstractItemHolder {

        /**
         * Callback to be invoked once an item gets selected.
         */
        var callback: () -> Unit = {}

        internal abstract fun convertToPopupMenuItem(): MaterialPopupMenu.AbstractPopupMenuItem

    }

}

/**
 * Function to create a [MaterialPopupMenuBuilder].
 * @param init block containing popup menu definition
 */
fun popupMenuBuilder(init: MaterialPopupMenuBuilder.() -> Unit): MaterialPopupMenuBuilder {
    val popupMenu = MaterialPopupMenuBuilder()
    popupMenu.init()
    return popupMenu
}

/**
 * Function to create a [MaterialPopupMenu].
 * @param init block containing popup menu definition
 */
fun popupMenu(init: MaterialPopupMenuBuilder.() -> Unit): MaterialPopupMenu {
    return popupMenuBuilder(init).build()
}

@DslMarker
internal annotation class PopupMenuMarker
