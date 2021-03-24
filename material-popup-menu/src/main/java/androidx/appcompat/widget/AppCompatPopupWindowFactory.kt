package androidx.appcompat.widget

import android.content.Context
import com.github.zawadz88.materialpopupmenu.PopupAnimation

internal fun createAppCompatPopupWindow(context: Context, customAnimation: PopupAnimation?) = MaterialPopupWindow(context, customAnimation)
