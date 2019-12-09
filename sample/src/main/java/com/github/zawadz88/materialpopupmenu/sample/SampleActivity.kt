package com.github.zawadz88.materialpopupmenu.sample

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.sample.extensions.roundPixels
import com.github.zawadz88.materialpopupmenu.sample.extensions.toPixelFromDip
import com.github.zawadz88.materialpopupmenu.sample.persistence.DEFAULT_POPUP_STYLE
import com.github.zawadz88.materialpopupmenu.sample.persistence.SelectionRepository
import com.github.zawadz88.materialpopupmenu.sample.persistence.SelectionRepositoryImpl
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.android.synthetic.main.activity_sample.anchorWidthInputEditText
import kotlinx.android.synthetic.main.activity_sample.container
import kotlinx.android.synthetic.main.activity_sample.gravityToggleGroup
import kotlinx.android.synthetic.main.activity_sample.popupStyleColoredButton
import kotlinx.android.synthetic.main.activity_sample.popupStyleDarkButton
import kotlinx.android.synthetic.main.activity_sample.popupStyleDefaultButton
import kotlinx.android.synthetic.main.activity_sample.popupStyleLightButton
import kotlinx.android.synthetic.main.activity_sample.popupStyleToggleGroup
import kotlinx.android.synthetic.main.activity_sample.popupTypeRadioGroup
import kotlinx.android.synthetic.main.activity_sample.showPopupButton
import kotlinx.android.synthetic.main.activity_sample.themeDarkButton
import kotlinx.android.synthetic.main.activity_sample.themeLightButton
import kotlinx.android.synthetic.main.activity_sample.themeToggleGroup
import kotlinx.android.synthetic.main.activity_sample.toolbar

class SampleActivity : AppCompatActivity() {

    private val selectionRepository: SelectionRepository by lazy { SelectionRepositoryImpl(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeToUse = selectionRepository.theme
        setTheme(themeToUse)
        setContentView(R.layout.activity_sample)
        setSupportActionBar(toolbar)
        initializeThemeButtonGroup(themeToUse)
        initializePopupStyleButtonGroup()
        initializeGravityButtonGroup()
        initializePopupRadioGroup()
        initializeShowPopupButton()
        initializeAnchorViewWidthInputEditText()
    }

    private fun initializeGravityButtonGroup() {
        selectionRepository.dropdownGravities
            .map { GRAVITY_TO_GRAVITY_BUTTON_ID_MAP.getValue(it) }
            .forEach { gravityToggleGroup.check(it) }
        gravityToggleGroup.addOnButtonCheckedListener { group, _, _ ->
            selectionRepository.dropdownGravities = group.checkedButtonIds.map { GRAVITY_BUTTON_ID_TO_GRAVITY_MAP.getValue(it) }
        }
    }

    private fun initializeAnchorViewWidthInputEditText() {
        anchorWidthInputEditText.addTextChangedListener(afterTextChanged = { editable ->
            if (editable.isNullOrEmpty()) return@addTextChangedListener

            editable.toString().toInt().let {
                selectionRepository.anchorWidthInDp = it
                showPopupButton.layoutParams.width = toPixelFromDip(it).roundPixels()
                showPopupButton.requestLayout()
            }
        })
        anchorWidthInputEditText.setText(selectionRepository.anchorWidthInDp.toString())
    }

    private fun initializeShowPopupButton() {
        container.addDraggableChild(showPopupButton)
        showPopupButton.setOnClickListener(::showPopupMenu)
    }

    private fun showPopupMenu(clickedView: View) {
        val checkedSamplePosition = selectionRepository.samplePosition
        val materialPopupMenuBuilder = SAMPLES[checkedSamplePosition].popupMenuProvider(this, clickedView)
        val popupMenuStyle = selectionRepository.popupStyle
        if (isCustomPopupMenuStyleUsed(popupMenuStyle) && canOverrideDefaultPopupStyle(materialPopupMenuBuilder)) {
            materialPopupMenuBuilder.style = popupMenuStyle
        }
        var jointGravity = 0
        selectionRepository.dropdownGravities.forEach { gravity -> jointGravity = jointGravity or gravity }
        if (jointGravity != 0) {
            materialPopupMenuBuilder.dropdownGravity = jointGravity
        }
        materialPopupMenuBuilder.build().run {
            show(this@SampleActivity, clickedView)
            setOnDismissListener { Log.i(TAG, "Popup dismissed!") }
        }
    }

    private fun isCustomPopupMenuStyleUsed(popupMenuStyle: Int) = popupMenuStyle != DEFAULT_POPUP_STYLE

    private fun canOverrideDefaultPopupStyle(materialPopupMenuBuilder: MaterialPopupMenuBuilder) = materialPopupMenuBuilder.style == 0

    private fun initializeThemeButtonGroup(themeToUse: Int) {
        val themeButtonIdToCheck = THEME_ID_TO_THEME_BUTTON_ID_MAP.getValue(themeToUse)
        themeToggleGroup.check(themeButtonIdToCheck)
        // TODO this can be simplified once https://github.com/material-components/material-components-android/commit/d4a17635b1a058a88ca03985a83c3990ba6626b7 is released
        listOf(themeDarkButton, themeLightButton).forEach { button ->
            button.setOnClickListener {
                val checkedButtonId = themeToggleGroup.checkedButtonId
                if (checkedButtonId == 0 || checkedButtonId == View.NO_ID) {
                    // Make sure one theme is always checked.
                    themeToggleGroup.check(button.id)
                } else {
                    changeThemeTo(THEME_BUTTON_ID_TO_THEME_ID_MAP.getValue(button.id))
                }
            }
        }
    }

    private fun initializePopupStyleButtonGroup() {
        popupStyleToggleGroup.check(POPUP_STYLE_ID_TO_POPUP_STYLE_BUTTON_ID_MAP.getValue(selectionRepository.popupStyle))
        popupStyleToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectionRepository.popupStyle = POPUP_STYLE_BUTTON_ID_TO_POPUP_STYLE_ID_MAP.getValue(checkedId)
            }
        }
        // TODO this can be simplified once https://github.com/material-components/material-components-android/commit/d4a17635b1a058a88ca03985a83c3990ba6626b7 is released
        listOf(popupStyleDefaultButton, popupStyleLightButton, popupStyleDarkButton, popupStyleColoredButton).forEach { button ->
            button.setOnClickListener {
                val checkedButtonId = popupStyleToggleGroup.checkedButtonId
                if (checkedButtonId == 0 || checkedButtonId == View.NO_ID) {
                    // Make sure one theme is always checked.
                    popupStyleToggleGroup.check(button.id)
                }
            }
        }
    }

    private fun initializePopupRadioGroup() {
        SAMPLES.forEachIndexed { index, item ->
            val radioButton = createRadioButton(index, item)
            popupTypeRadioGroup.addView(radioButton)
        }
        popupTypeRadioGroup.check(selectionRepository.samplePosition)
        popupTypeRadioGroup.setOnCheckedChangeListener { group, _ ->
            val checkedRadioButton = group.findViewById<RadioButton>(popupTypeRadioGroup.checkedRadioButtonId)
            selectionRepository.samplePosition = popupTypeRadioGroup.indexOfChild(checkedRadioButton)
        }
    }

    private fun createRadioButton(index: Int, sample: PopupMenuSample): MaterialRadioButton = MaterialRadioButton(popupTypeRadioGroup.context).apply {
        id = index
        setText(sample.label)
    }

    private fun changeThemeTo(@StyleRes newThemeStyle: Int) {
        selectionRepository.theme = newThemeStyle
        val intent = Intent(this, SampleActivity::class.java)
        val activityOptions = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent, activityOptions.toBundle())
        finish()
    }
}

private const val TAG = "MPM"

private val THEME_BUTTON_ID_TO_THEME_ID_MAP: Map<Int, Int> = mapOf(
    R.id.themeDarkButton to R.style.AppTheme_Dark,
    R.id.themeLightButton to R.style.AppTheme_Light
)

private val THEME_ID_TO_THEME_BUTTON_ID_MAP: Map<Int, Int> = mapOf(
    R.style.AppTheme_Dark to R.id.themeDarkButton,
    R.style.AppTheme_Light to R.id.themeLightButton
)

private val POPUP_STYLE_BUTTON_ID_TO_POPUP_STYLE_ID_MAP: Map<Int, Int> = mapOf(
    R.id.popupStyleDefaultButton to DEFAULT_POPUP_STYLE,
    R.id.popupStyleLightButton to R.style.Widget_MPM_Menu,
    R.id.popupStyleDarkButton to R.style.Widget_MPM_Menu_Dark,
    R.id.popupStyleColoredButton to R.style.Widget_MPM_Menu_Dark_ColoredBackground
)

private val POPUP_STYLE_ID_TO_POPUP_STYLE_BUTTON_ID_MAP: Map<Int, Int> = mapOf(
    DEFAULT_POPUP_STYLE to R.id.popupStyleDefaultButton,
    R.style.Widget_MPM_Menu to R.id.popupStyleLightButton,
    R.style.Widget_MPM_Menu_Dark to R.id.popupStyleDarkButton,
    R.style.Widget_MPM_Menu_Dark_ColoredBackground to R.id.popupStyleColoredButton
)

private val GRAVITY_BUTTON_ID_TO_GRAVITY_MAP: Map<Int, Int> = mapOf(
    R.id.gravityStartButton to Gravity.START,
    R.id.gravityEndButton to Gravity.END,
    R.id.gravityBottomButton to Gravity.BOTTOM,
    R.id.gravityTopButton to Gravity.TOP
)

private val GRAVITY_TO_GRAVITY_BUTTON_ID_MAP: Map<Int, Int> = mapOf(
    Gravity.START to R.id.gravityStartButton,
    Gravity.END to R.id.gravityEndButton,
    Gravity.BOTTOM to R.id.gravityBottomButton,
    Gravity.TOP to R.id.gravityTopButton
)
