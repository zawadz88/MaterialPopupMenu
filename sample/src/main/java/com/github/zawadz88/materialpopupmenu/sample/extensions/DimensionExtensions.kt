package com.github.zawadz88.materialpopupmenu.sample.extensions

import android.content.Context
import android.util.TypedValue
import android.view.View
import kotlin.math.ceil
import kotlin.math.floor

fun Context.toPixelFromDip(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

fun Context.toPixelFromDip(value: Int): Float = toPixelFromDip(value.toFloat())

fun View.toPixelFromDip(value: Float): Float = context.toPixelFromDip(value)

fun View.toPixelFromDip(value: Int): Float = toPixelFromDip(value.toFloat())

fun Float.roundPixels(): Int = (if (this < 0) ceil(this - 0.5f) else floor(this + 0.5f)).toInt()
