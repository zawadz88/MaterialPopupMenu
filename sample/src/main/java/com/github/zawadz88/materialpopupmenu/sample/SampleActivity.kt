package com.github.zawadz88.materialpopupmenu.sample

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sample.themeDarkButton
import kotlinx.android.synthetic.main.activity_sample.themeLightButton
import kotlinx.android.synthetic.main.activity_sample.themeToggleGroup
import kotlinx.android.synthetic.main.activity_sample.toolbar

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeToUse = intent.getIntExtra(THEME_EXTRA_KEY, R.style.AppTheme_Light)
        setTheme(themeToUse)
        setContentView(R.layout.activity_sample)
        setSupportActionBar(toolbar)
        initializeThemeButtonGroup(themeToUse)
    }

    private fun initializeThemeButtonGroup(themeToUse: Int) {
        val themeButtonIdToCheck = THEME_ID_TO_THEME_BUTTON_ID_MAP.getValue(themeToUse)
        themeToggleGroup.check(themeButtonIdToCheck)
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

    private fun changeThemeTo(@StyleRes newThemeStyle: Int) {
        val intent = Intent(this, SampleActivity::class.java)
            .putExtra(THEME_EXTRA_KEY, newThemeStyle)
        val activityOptions = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent, activityOptions.toBundle())
        finish()
    }
}

private val THEME_BUTTON_ID_TO_THEME_ID_MAP: Map<Int, Int> = mapOf(
    R.id.themeDarkButton to R.style.AppTheme_Dark,
    R.id.themeLightButton to R.style.AppTheme_Light
)

private val THEME_ID_TO_THEME_BUTTON_ID_MAP: Map<Int, Int> = mapOf(
    R.style.AppTheme_Dark to R.id.themeDarkButton,
    R.style.AppTheme_Light to R.id.themeLightButton
)

private const val THEME_EXTRA_KEY = "theme"
