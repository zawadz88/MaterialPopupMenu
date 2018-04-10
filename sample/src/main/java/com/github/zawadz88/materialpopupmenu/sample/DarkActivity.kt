package com.github.zawadz88.materialpopupmenu.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.github.zawadz88.materialpopupmenu.popupMenuBuilder

@SuppressLint("PrivateResource")
class DarkActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.copyConditionalSwitch)
    lateinit var copyConditionalSwitch: SwitchCompat

    @BindView(R.id.sectionConditionalSwitch)
    lateinit var sectionConditionalSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_overflow, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.overflow) {
            val popupMenu = popupMenu {
                style = R.style.Widget_MPM_Menu_Dark
                dropdownGravity = Gravity.END
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
                }
            }

            popupMenu.show(this@DarkActivity, checkNotNull(findViewById(R.id.overflow)))
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
                item {
                    label = "Select all"
                    icon = R.drawable.abc_ic_menu_selectall_mtrl_alpha
                }
            }
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
            }
        }

        popupMenu.show(this@DarkActivity, view)
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
                    viewBoundCallback = { view ->
                        val checkBox: CheckBox = view.findViewById(R.id.customItemCheckbox)
                        checkBox.isChecked = true
                    }
                    callback = {
                        Toast.makeText(this@DarkActivity, "Disabled!", Toast.LENGTH_SHORT).show()
                    }
                }
                customItem {
                    layoutResId = R.layout.view_custom_item_large
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

    private fun shareUrl() {
        val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SHARE_URL))
        startActivity(shareIntent)
    }
}
