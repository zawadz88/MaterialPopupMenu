package com.github.zawadz88.materialpopupmenu

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Piotr Zawadzki
 */
class MaterialPopupMenuBuilderTest {

    companion object {
        private const val ITEM_LABEL = "item label"
        private const val ITEM_LABEL2 = "item label2"
        private const val ITEM_LABEL3 = "item label3"
        private const val ITEM_LABEL_RES = 777
        private const val ITEM_LABEL_TEXT_COLOR = 123
        private const val ITEM_ICON_TINT_COLOR = 888
        private const val ITEM_ICON = -1
        private val ITEM_ICON_DRAWABLE: Drawable = mock { }
        private const val CUSTOM_ITEM_LAYOUT = 555
        private const val SECTION_TITLE = "section title"
        private const val SECTION_TITLE2 = "section title2"
        private const val CUSTOM_WIDTH = 500
        private const val CUSTOM_OFFSET = 200
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Building an empty popup menu should throw an exception`() {
        popupMenu { }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Building a popup menu with a single empty section should throw an exception`() {
        popupMenu {
            section { }
        }
    }

    @Test(expected = IllegalArgumentException::class)
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

    @Test(expected = IllegalArgumentException::class)
    fun `Building a popup menu with an empty item in section should throw an exception`() {
        popupMenu {
            section {
                item { }
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Building a popup menu with an empty custom item in section should throw an exception`() {
        popupMenu {
            section {
                customItem { }
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
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
        assertEquals("Invalid popup style", 0, popupMenu.style)
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
    fun `Should build a popup menu with a custom fixedContentWidthInPx`() {
        //when
        val popupMenu = popupMenu {
            fixedContentWidthInPx = CUSTOM_WIDTH
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid popup fixedContentWidthInPx value", CUSTOM_WIDTH, popupMenu.fixedContentWidthInPx)
    }

    @Test
    fun `Should build a popup menu with a custom dropDownHorizontalOffset`() {
        //when
        val popupMenu = popupMenu {
            dropDownHorizontalOffset = CUSTOM_OFFSET
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid popup dropDownHorizontalOffset value", CUSTOM_OFFSET, popupMenu.dropDownHorizontalOffset)
    }

    @Test
    fun `Should build a popup menu with a custom dropDownVerticalOffset`() {
        //when
        val popupMenu = popupMenu {
            dropDownVerticalOffset = CUSTOM_OFFSET
            section {
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertEquals("Invalid popup dropDownVerticalOffset value", CUSTOM_OFFSET, popupMenu.dropDownVerticalOffset)
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
    fun `Should build a popup menu with multiple custom items and a single item`() {
        //given
        val customCallback = {}

        //when
        val popupMenu = popupMenu {
            section {
                customItem {
                    layoutResId = CUSTOM_ITEM_LAYOUT
                    callback = customCallback
                }
                customItem {
                    layoutResId = CUSTOM_ITEM_LAYOUT
                    callback = customCallback
                }
                item {
                    label = ITEM_LABEL
                }
            }
        }

        //then
        assertThat("Should contain a single section", popupMenu.sections, hasSize(1))
        val section = popupMenu.sections[0]
        assertThat("Invalid item count", section.items, hasSize(3))

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
        val thirdItem = section.items[2]
        assertThat(thirdItem, instanceOf(MaterialPopupMenu.PopupMenuItem::class.java))
        val thirdPopupMenuItem = thirdItem as MaterialPopupMenu.PopupMenuItem
        assertEquals("Invalid item label", ITEM_LABEL, thirdPopupMenuItem.label)
    }

    @Test
    fun `Should invoke custom view bound callback on a custom item`() {
        //given
        val view = mock<View> {}
        var callbackInvoked = false
        val customViewBoundCallback: (View) -> Unit = { callbackInvoked = true }
        val popupMenu = popupMenu {
            section {
                customItem {
                    layoutResId = CUSTOM_ITEM_LAYOUT
                    viewBoundCallback = customViewBoundCallback
                }
            }
        }
        val item = popupMenu.sections[0].items[0]
        val popupMenuItem = item as MaterialPopupMenu.PopupMenuCustomItem

        //when
        popupMenuItem.viewBoundCallback.invoke(view)

        //then
        assertTrue("View bound callback has not been invoked", callbackInvoked)
    }

    @Test
    fun `Should invoke custom view bound callback on a default item`() {
        //given
        val view = mock<View> {}
        var callbackInvoked = false
        val customViewBoundCallback: (View) -> Unit = { callbackInvoked = true }
        val popupMenu = popupMenu {
            section {
                item {
                    label = ITEM_LABEL
                    viewBoundCallback = customViewBoundCallback
                }
            }
        }
        val item = popupMenu.sections[0].items[0]
        val popupMenuItem = item as MaterialPopupMenu.PopupMenuItem

        //when
        popupMenuItem.viewBoundCallback.invoke(view)

        //then
        assertTrue("View bound callback has not been invoked", callbackInvoked)
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
        val (_, _, _, _, _, _, _, _, callback, _) = popupMenuItem

        //when
        callback()

        //then
        //nothing to do here...
    }

    @Test
    fun `Should build a popup menu with a String resource label`() {
        //when
        val popupMenu = popupMenu {
            section {
                item {
                    labelRes = ITEM_LABEL_RES
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
        assertNull("Invalid item label", popupMenuItem.label)
        assertEquals("Invalid item label resource", ITEM_LABEL_RES, popupMenuItem.labelRes)
    }
}
