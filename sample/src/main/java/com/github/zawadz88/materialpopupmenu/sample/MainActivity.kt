package com.github.zawadz88.materialpopupmenu.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class MainActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
    }

    @OnClick(R.id.light_activity)
    fun onLightActivityClicked() {
        startActivity(Intent(this, LightActivity::class.java))
    }

    @OnClick(R.id.dark_activity)
    fun onDarkActivityClicked() {
        startActivity(Intent(this, DarkActivity::class.java))
    }
}