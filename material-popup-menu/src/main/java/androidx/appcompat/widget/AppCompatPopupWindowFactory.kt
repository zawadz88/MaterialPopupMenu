package androidx.appcompat.widget

import android.content.Context
import android.widget.PopupWindow

internal fun createAppCompatPopupWindow(context: Context): PopupWindow = AppCompatPopupWindow(context, null, 0)
