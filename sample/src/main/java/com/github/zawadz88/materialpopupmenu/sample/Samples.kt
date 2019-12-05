package com.github.zawadz88.materialpopupmenu.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.ViewBoundCallback
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.github.zawadz88.materialpopupmenu.popupMenuBuilder

data class PopupMenuSample(@StringRes val label: Int, val popupMenuProvider: (Context, View) -> MaterialPopupMenuBuilder)

@SuppressLint("SetTextI18n", "PrivateResource")
val SAMPLES: List<PopupMenuSample> = listOf(
    PopupMenuSample(R.string.single_section_with_icons) { context, _ ->
        popupMenuBuilder {
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                    icon = R.drawable.abc_ic_menu_selectall_mtrl_alpha
                }
            }
        }
    },
    PopupMenuSample(R.string.single_section_without_icons) { context, _ ->
        popupMenuBuilder {
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                }
                item {
                    label = "Stay open when clicked"
                    dismissOnSelect = false
                    callback = {
                        Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.two_sections_with_section_titles) { context, _ ->
        popupMenuBuilder {
            section {
                title = "Editing"
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                title = "Other"
                item {
                    label = "Share"
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    callback = {
                        shareUrl(context)
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.two_sections_without_titles) { context, _ ->
        popupMenuBuilder {
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                item {
                    label = "Share"
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    callback = {
                        shareUrl(context)
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.custom_colors) { context, _ ->
        popupMenuBuilder {
            section {
                item {
                    label = "Copy"
                    labelColor = ContextCompat.getColor(context, R.color.red)
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    iconColor = ContextCompat.getColor(context, R.color.dark_red)
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    labelColor = ContextCompat.getColor(context, R.color.red)
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    iconColor = ContextCompat.getColor(context, R.color.dark_red)
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                item {
                    label = "Share"
                    labelColor = ContextCompat.getColor(context, R.color.green)
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    iconColor = ContextCompat.getColor(context, R.color.dark_green)
                    callback = {
                        shareUrl(context)
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.custom_items) { context, _ ->
        popupMenuBuilder {
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_checkable
                    viewBoundCallback = ViewBoundCallback { view ->
                        val checkBox: CheckBox = view.findViewById(R.id.customItemCheckbox)
                        checkBox.isChecked = true
                        checkBox.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                dismissPopup()
                            }
                        }
                    }
                    callback = {
                        Toast.makeText(context, "Disabled!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_large
                    viewBoundCallback = { view ->
                        val textView: TextView = view.findViewById(R.id.customItemTextView)
                        textView.text = "Some long text that is applied later to see if height calculation indeed is incorrectly calculated due to this binding."
                    }
                }
                item {
                    label = createMultiLineText(context)
                    viewBoundCallback = { view ->
                        val tv = view.findViewById<TextView>(R.id.mpm_popup_menu_item_label)
                        tv.setSingleLine(false)
                        tv.maxLines = 2
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.custom_offsets) { context, _ ->
        popupMenuBuilder {
            style = R.style.Widget_MPM_Menu_CustomOffsets
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                }
            }
        }
    },
    PopupMenuSample(R.string.dimmed_background) { context, _ ->
        popupMenuBuilder {
            style = R.style.Widget_MPM_Menu_Dimmed
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.custom_padding) { context, _ ->
        popupMenuBuilder {
            style = R.style.Widget_MPM_Menu_CustomPadding
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.rounded_corners) { context, _ ->
        popupMenuBuilder {
            style = R.style.Widget_MPM_Menu_RoundedCorners
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_checkable
                    viewBoundCallback = { view ->
                        val checkBox: CheckBox = view.findViewById(R.id.customItemCheckbox)
                        checkBox.isChecked = true
                    }
                    callback = {
                        Toast.makeText(context, "Disabled!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_colored
                }
            }
        }
    }, PopupMenuSample(R.string.anchor_width) { context, anchor ->
        popupMenuBuilder {
            fixedContentWidthInPx = anchor.width
            dropDownHorizontalOffset = 0
            dropDownVerticalOffset = calculateVerticalOffsetForBottomButton(anchor)
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    },
    PopupMenuSample(R.string.multi_level) { context, anchor ->
        popupMenuBuilder {
            section {
                title = "Editing"
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    iconDrawable = ContextCompat.getDrawable(context, R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                }
            }
            section {
                item {
                    label = "Share me please"
                    callback = {
                        shareUrl(context)
                    }
                }
                item {
                    label = "Multi-level"
                    hasNestedItems = true
                    callback = {
                        val nestedPopupMenu = popupMenu {
                            dropdownGravity = this@popupMenuBuilder.dropdownGravity
                            section {
                                title = "Second level"
                                item {
                                    label = "Copy"
                                }
                                item {
                                    label = "Paste"
                                }
                            }
                        }

                        nestedPopupMenu.show(context, anchor)
                    }
                }
                item {
                    label = "Multi-level"
                    hasNestedItems = true
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        val nestedPopupMenu = popupMenu {
                            dropdownGravity = this@popupMenuBuilder.dropdownGravity
                            section {
                                title = "Second level"
                                item {
                                    label = "Copy"
                                }
                                item {
                                    label = "Paste"
                                }
                            }
                        }

                        nestedPopupMenu.show(context, anchor)
                    }
                }
            }
        }
    }
)

private fun shareUrl(context: Context) {
    val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SHARE_URL))
    context.startActivity(shareIntent)
}

@SuppressLint("PrivateResource")
private fun createMultiLineText(context: Context): CharSequence {
    val text = "Line 1\nLines in secondary color"
    val spannable = SpannableString(text)
    val idx = text.indexOf("\n")
    if (idx != -1) {
        spannable.setSpan(
            RelativeSizeSpan(0.7f),
            idx,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.abc_secondary_text_material_light)),
            idx,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

/**
 * There was a change in dropdown offsets on Nougat. Before it the popup menu would be displayed on top of the View and not above it.
 */
private fun calculateVerticalOffsetForBottomButton(view: View) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) view.height else 0

private const val SHARE_URL = "https://github.com/zawadz88"
