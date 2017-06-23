package com.github.zawadz88.materialpopupmenu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onClickListener = View.OnClickListener { view ->

            val popupMenu = popupMenu {
                style = R.style.MaterialPopupMenuStyle
                section {
                    title = "ssddd"
                    item {
                        label = "Label1"
                        icon = R.drawable.abc_ic_voice_search_api_material
                        callback = {
                            println("dfdsf")
                        }
                    }
                    item {
                        label = "Label2"
                        icon = R.drawable.abc_ic_voice_search_api_material
                        callback = {
                            Toast.makeText(this@MainActivity, "yoooo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                section {
                    item {
                        label = "Preview kjdkjasld sds adas dsa s"
                    }
                    item {
                        label = "Preview2"
                    }
                }
            }

            popupMenu.show(this@MainActivity, view)

        }
        val button = findViewById(R.id.button)
        val buttonBottom = findViewById(R.id.buttonBottom)
        val buttonStart = findViewById(R.id.buttonStart)
        val buttonEnd = findViewById(R.id.buttonEnd)
        button.setOnClickListener(onClickListener)
        buttonBottom.setOnClickListener(onClickListener)
        buttonStart.setOnClickListener(onClickListener)
        buttonEnd.setOnClickListener(onClickListener)
    }
}