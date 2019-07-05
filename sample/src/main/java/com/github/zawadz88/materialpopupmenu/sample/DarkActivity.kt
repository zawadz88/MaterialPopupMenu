package com.github.zawadz88.materialpopupmenu.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import butterknife.BindDimen
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.zawadz88.materialpopupmenu.ViewBoundCallback
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.github.zawadz88.materialpopupmenu.popupMenuBuilder

@SuppressLint("PrivateResource")
class DarkActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MPM"
    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.copyConditionalSwitch)
    lateinit var copyConditionalSwitch: SwitchCompat

    @BindView(R.id.sectionConditionalSwitch)
    lateinit var sectionConditionalSwitch: SwitchCompat

    @BindView(R.id.roundedCornersTextView)
    lateinit var roundedCornersTextView: TextView

    @JvmField
    @BindDimen(R.dimen.horizontal_button_padding)
    var horizontalButtonPadding: Int = 0

    @JvmField
    @BindDimen(R.dimen.horizontal_popup_menu_background_start_padding)
    var horizontalPopupMenuBackgroundStartPadding: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        roundedCornersTextView.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_overflow, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.overflow) {
            val anchor = checkNotNull<View>(findViewById(R.id.overflow))
            val popupMenu = popupMenu {
                style = R.style.Widget_MPM_Menu_Dark
                dropdownGravity = Gravity.END
                section {
                    title = "Editing"
                    item {
                        labelRes = R.string.copy
                        icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                        callback = {
                            Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    item {
                        label = "Paste"
                        iconDrawable = ContextCompat.getDrawable(this@DarkActivity, R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                    }
                }
                section {
                    item {
                        label = "Share me please"
                        callback = {
                            shareUrl()
                        }
                    }
                    item {
                        label = "Multi-level"
                        hasNestedItems = true
                        callback = {
                            val nestedPopupMenu = popupMenu {
                                style = R.style.Widget_MPM_Menu_Dark
                                dropdownGravity = Gravity.END
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

                            nestedPopupMenu.show(this@DarkActivity, anchor)
                        }
                    }
                    item {
                        label = "Multi-level"
                        hasNestedItems = true
                        icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                        callback = {
                            val nestedPopupMenu = popupMenu {
                                style = R.style.Widget_MPM_Menu_Dark
                                dropdownGravity = Gravity.END
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

                            nestedPopupMenu.show(this@DarkActivity, anchor)
                        }
                    }

                }
            }

            popupMenu.show(this@DarkActivity, anchor)
            return true
        }
        return false
    }

    @OnClick(R.id.singleSectionWithIcons)
    fun onSingleSectionWithIconsClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                item {
                    labelRes = R.string.copy
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                    icon = R.drawable.abc_ic_menu_selectall_mtrl_alpha
                }
            }
        }

        popupMenu.setOnDismissListener {
            Log.i(TAG, "Popup dismissed!")
        }
        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.singleSectionWithoutIcons)
    fun onSingleSectionWithoutIconsClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                }
                item {
                    label = "Stay open when clicked"
                    dismissOnSelect = false
                    callback = {
                        Toast.makeText(this@DarkActivity, "Clicked!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
        popupMenu.setOnDismissListener {
            Log.i(TAG, "Popup dismissed!")
        }
    }

    @OnClick(R.id.sectionsWithTitlesTextView)
    fun onSectionWithTitlesClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                title = "Editing"
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                title = "Other"
                item {
                    label = "Share"
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    callback = {
                        shareUrl()
                    }
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.sectionsWithoutTitlesTextView)
    fun onSectionWithoutTitlesClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                item {
                    label = "Share"
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    callback = {
                        shareUrl()
                    }
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.customColorsTextView)
    fun onCustomColorsClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark_CustomBackground
            section {
                item {
                    label = "Copy"
                    labelColor = ContextCompat.getColor(this@DarkActivity, R.color.red)
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    iconColor = ContextCompat.getColor(this@DarkActivity, R.color.dark_red)
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    labelColor = ContextCompat.getColor(this@DarkActivity, R.color.red)
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    iconColor = ContextCompat.getColor(this@DarkActivity, R.color.dark_red)
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            section {
                item {
                    label = "Share"
                    labelColor = ContextCompat.getColor(this@DarkActivity, R.color.green)
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    iconColor = ContextCompat.getColor(this@DarkActivity, R.color.dark_green)
                    callback = {
                        shareUrl()
                    }
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.customOffsetsTextView)
    fun onCustomOffsetsTextViewClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark_CustomOffsets
            dropdownGravity = Gravity.BOTTOM
            section {
                item {
                    label = "Copy"
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Select all"
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.dimmedBackgroundTextView)
    fun onDimmedBackgroundClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark_Dimmed
            section {
                item {
                    label = "Copy"
                }
                item {
                    label = "Paste"
                }
            }
        }
        popupMenu.show(this@DarkActivity, view)
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.customItemsTextView)
    fun onCustomItemsClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            dropdownGravity = Gravity.END
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@DarkActivity, "Disabled!", Toast.LENGTH_SHORT).show()
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
                    label = createMultiLineText()
                    viewBoundCallback = { view ->
                        val tv = view.findViewById<TextView>(R.id.mpm_popup_menu_item_label)
                        tv.setSingleLine(false)
                        tv.maxLines = 2
                    }
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.customPaddingTextView)
    fun onCustomPaddingTextViewClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark_CustomPadding
            section {
                item {
                    label = "Copy"
                }
                item {
                    label = "Paste"
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.roundedCornersTextView)
    fun onRoundedCornersClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark_RoundedCorners
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_checkable
                    viewBoundCallback = { view ->
                        val checkBox: CheckBox = view.findViewById(R.id.customItemCheckbox)
                        checkBox.isChecked = true
                    }
                    callback = {
                        Toast.makeText(this@DarkActivity, "Disabled!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_colored
                }
            }
        }

        popupMenu.show(this@DarkActivity, view)
    }

    @OnClick(R.id.conditionalItemsTextView)
    fun onConditionalItemsClicked(view: View) {
        val conditionalPopupMenuBuilder = popupMenuBuilder {
            style = R.style.Widget_MPM_Menu_Dark
            section {
                if (copyConditionalSwitch.isChecked) {
                    item {
                        label = "Copy"
                        icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                        callback = {
                            Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (sectionConditionalSwitch.isChecked) {
            conditionalPopupMenuBuilder.section {
                item {
                    label = "Share"
                    icon = R.drawable.abc_ic_menu_share_mtrl_alpha
                    callback = {
                        shareUrl()
                    }
                }
            }
        }

        conditionalPopupMenuBuilder.build().show(this@DarkActivity, view)
    }

    @OnClick(R.id.bottomButton)
    fun onBottomButtonClicked(view: View) {
        val popupMenu = popupMenu {
            style = R.style.Widget_MPM_Menu_Dark
            dropdownGravity = Gravity.TOP xor Gravity.CENTER_HORIZONTAL
            fixedContentWidthInPx = calculatePopupMenuWidthForBottomButton(view)
            dropDownHorizontalOffset = horizontalButtonPadding - horizontalPopupMenuBackgroundStartPadding
            dropDownVerticalOffset = calculatePopupMenuVerticalOffsetForBottomButton(view)
            section {
                item {
                    label = "Copy"
                    icon = R.drawable.abc_ic_menu_copy_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Copied!", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    label = "Paste"
                    icon = R.drawable.abc_ic_menu_paste_mtrl_am_alpha
                    callback = {
                        Toast.makeText(this@DarkActivity, "Text pasted!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        popupMenu.show(this@DarkActivity, view)
    }

    private fun calculatePopupMenuWidthForBottomButton(view: View) = view.width - 2 * horizontalButtonPadding

    /**
     * There was a change in dropdown offsets on Nougat. Before it the popup menu would be displayed on top of the View and not above it.
     */
    private fun calculatePopupMenuVerticalOffsetForBottomButton(view: View) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) view.height else 0

    private fun shareUrl() {
        val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SHARE_URL))
        startActivity(shareIntent)
    }

    private fun createMultiLineText(): CharSequence {
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
                ForegroundColorSpan(ContextCompat.getColor(this@DarkActivity, R.color.abc_secondary_text_material_dark)),
                idx,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }
}
