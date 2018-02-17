package com.github.zawadz88.materialpopupmenu

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * @author Piotr Zawadzki
 */
class MaterialPopupMenuBuilderTest {

    @Test(expected = IllegalStateException::class)
    fun `Building an empty popup menu should throw an exception`() {
        popupMenu { }
    }

    @Test(expected = IllegalStateException::class)
    fun `Building a popup menu with a single empty section should throw an exception`() {
        popupMenu {
            section { }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `Building a popup menu with one of the sections being empty should throw an exception`() {
        popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                }
            }
            section { }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `Building a popup menu with an empty item in section should throw an exception`() {
        popupMenu {
            section {
                item { }
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `Building a popup menu with an empty custom item in section should throw an exception`() {
        popupMenu {
            section {
                customItem { }
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `Building a popup menu with one of the items in section being empty should throw an exception`() {
        popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                }
                item { }
            }
        }
    }

    @Test
    fun `Should build a popup menu with a single simple item and default values`() {
        //when
        val popupMenu = popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid dropdown gravity", Gravity.NO_GRAVITY, popupMenu.dropdownGravity)
        assertEquals("Invalid popup style", R.style.Widget_MPM_Menu, popupMenu.style)
        assertThat("Should contain a single section", popupMenu.sections, hasSize(1))
        val (title, items) = popupMenu.sections[0]
        assertNull("Section title should be null", title)
        assertThat("Should contain a single item", items, hasSize(1))
        val item = items[0]
        assertThat(item, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val popupMenuItem = item as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL, popupMenuItem.label)
        assertEquals("Item icon should not be set", 0, popupMenuItem.icon)
    }

    @Test
    fun `Should build a popup menu with a custom style`() {
        //when
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid popup style", R.style.Widget_MPM_Menu_Dark, popupMenu.style)
    }

    @Test
    fun `Should build a popup menu with a custom dropdown gravity`() {
        //when
        val popupMenu = popupMenu {
            dropdownGravity = Gravity.END
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid dropdown gravity", Gravity.END, popupMenu.dropdownGravity)
    }

    @Test
    fun `Should build a popup menu with multiple sections with titles`() {
        //when
        val popupMenu = popupMenu {
            section {
                title = SECTION_TITLE
                item {
                    label = ITEM_LABEL
                }
            }
            section {
                title = SECTION_TITLE2
                item {
                    label = ITEM_LABEL2
                }
            }
        }

        //then
        assertThat("Should contain two sections", popupMenu.sections, hasSize(2))
        val firstSection = popupMenu.sections[0]
        assertEquals("Invalid section title", SECTION_TITLE, firstSection.title)
        assertThat("Should contain a single item", firstSection.items, hasSize(1))
        val secondSection = popupMenu.sections[1]
        assertEquals("Invalid section title", SECTION_TITLE2, secondSection.title)
        assertThat("Should contain a single item", secondSection.items, hasSize(1))
    }

    @Test
    fun `Should build a popup menu with a Drawable icon`() {
        //when
        val popupMenu = popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                    iconDrawable = ITEM_ICON_DRAWABLE
                }
            }
        }

        //then
        assertThat("Should contain one section", popupMenu.sections, hasSize(1))
        val section = popupMenu.sections[0]
        assertThat("Should contain a single item", section.items, hasSize(1))
        val item = section.items[0]
        assertThat(item, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val popupMenuItem = item as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL, popupMenuItem.label)
        assertEquals("Invalid item icon drawable", ITEM_ICON_DRAWABLE, popupMenuItem.iconDrawable)

    }

    @Test
    fun `Should build a popup menu with multiple items`() {
        //given
        val customCallback = {}

        //when
        val popupMenu = popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                    labelColor = ITEM_LABEL_TEXT_COLOR
                }
                item {
                    label = ITEM_LABEL2
                    icon = ITEM_ICON
                    iconColor = ITEM_ICON_TINT_COLOR
                }
                item {
                    label = ITEM_LABEL3
                    callback = customCallback
                }
            }
        }

        //then
        assertThat("Should contain a single section", popupMenu.sections, hasSize(1))
        val section = popupMenu.sections[0]
        assertThat("Invalid item count", section.items, hasSize(3))
        val firstItem = section.items[0]
        assertThat(firstItem, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val firstPopupMenuItem = firstItem as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL, firstPopupMenuItem.label)
        assertEquals("Invalid item label text color", ITEM_LABEL_TEXT_COLOR, firstPopupMenuItem.labelColor)
        val secondItem = section.items[1]
        assertThat(secondItem, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val secondPopupMenuItem = secondItem as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL2, secondPopupMenuItem.label)
        assertEquals("Invalid item icon", ITEM_ICON, secondPopupMenuItem.icon)
        assertEquals("Invalid item icon tint color", ITEM_ICON_TINT_COLOR, secondPopupMenuItem.iconColor)
        val thirdItem = section.items[2]
        assertThat(thirdItem, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val thirdPopupMenuItem = thirdItem as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL3, thirdPopupMenuItem.label)
        assertEquals("Invalid item callback", customCallback, thirdPopupMenuItem.callback)
    }


    @Test
    fun `Should build a popup menu with multiple custom items`() {
        //given
        val customCallback = {}
        val customViewBoundCallback: (View) -> Unit = {}

        //when
        val popupMenu = popupMenu {
            section {
                customItem {
                    layoutResId = CUSTOM_ITEM_LAYOUT
                    callback = customCallback
                }
                customItem {
                    layoutResId = CUSTOM_ITEM_LAYOUT
                    viewBoundCallback = customViewBoundCallback
                    callback = customCallback
                }
            }
        }

        //then
        assertThat("Should contain a single section", popupMenu.sections, hasSize(1))
        val section = popupMenu.sections[0]
        assertThat("Invalid item count", section.items, hasSize(2))

        val firstItem = section.items[0]
        assertThat(firstItem, instanceOf(MaterialPopupMenu.PopupMenuCustomItem::class.java))
        val firstPopupMenuItem = firstItem as MaterialPopupMenu.PopupMenuCustomItem
        assertEquals("Invalid item layout ID", CUSTOM_ITEM_LAYOUT, firstPopupMenuItem.layoutResId)
        assertEquals("Invalid item callback", customCallback, firstPopupMenuItem.callback)
        val secondItem = section.items[1]
        assertThat(secondItem, instanceOf(MaterialPopupMenu.PopupMenuCustomItem::class.java))
        val secondPopupMenuItem = secondItem as MaterialPopupMenu.PopupMenuCustomItem
        assertEquals("Invalid item layout ID", CUSTOM_ITEM_LAYOUT, secondPopupMenuItem.layoutResId)
        assertEquals("Invalid item callback", customCallback, secondPopupMenuItem.callback)
        assertEquals("Invalid item view bound callback", customViewBoundCallback, secondPopupMenuItem.viewBoundCallback)
    }

    @Test
    fun `Default item callback in popup menu should be empty`() {
        //given
        val popupMenu = popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }
        assertThat("Should contain a single section", popupMenu.sections, hasSize(1))
        val section = popupMenu.sections[0]
        assertThat("Should contain a single item", section.items, hasSize(1))
        val item = section.items[0]
        assertThat(item, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val popupMenuItem = item as MaterialPopupMenu.PopupMenuItem
        val (_, _, _, _, _, callback) = popupMenuItem

        //when
        callback()

        //then
        //nothing to do here...
    }

    companion object {
        val ITEM_LABEL = "item label"
        val ITEM_LABEL2 = "item label2"
        val ITEM_LABEL3 = "item label3"
        val ITEM_LABEL_TEXT_COLOR = 123
        val ITEM_ICON_TINT_COLOR = 888
        val ITEM_ICON = -1
        val ITEM_ICON_DRAWABLE: Drawable = mock { }
        val CUSTOM_ITEM_LAYOUT = 555
        val SECTION_TITLE = "section title"
        val SECTION_TITLE2 = "section title2"
    }

}