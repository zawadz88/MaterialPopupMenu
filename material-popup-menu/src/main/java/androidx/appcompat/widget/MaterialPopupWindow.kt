package androidx.appcompat.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.doOnLayout
import com.github.zawadz88.materialpopupmenu.PopupAnimation

internal class MaterialPopupWindow(
        context: Context,
        private val customAnimation: PopupAnimation?
) : AppCompatPopupWindow(context, null, 0) {

    override fun showAsDropDown(anchor: View?) {
        prepareAnimation()
        super.showAsDropDown(anchor)
        startAnimation()
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        prepareAnimation()
        super.showAsDropDown(anchor, xoff, yoff)
        startAnimation()
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        prepareAnimation()
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        startAnimation()
    }

    override fun dismiss() {
        customAnimation?.let {
            it.onHide(this) {
                super.dismiss()
                contentView = null
            }
        } ?: run {
            super.dismiss()
            contentView = null
        }
    }

    private fun prepareAnimation() {
        customAnimation?.onPrepare(this)
        if (customAnimation != null) {
            disableAnimations()
        }
    }

    private fun startAnimation() {
        customAnimation?.let { anim ->
            disableAnimations()
            anim.onPrepare(this)
            contentView?.doOnLayout {
                anim.onShow(this)
            }
        }
    }

    private fun disableAnimations() {
        animationStyle = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            enterTransition = null
            exitTransition = null
        } else {
            try {
                val field1 = PopupWindow::class.java.getDeclaredField("mPopupView")
                field1.isAccessible = true
                val popupView = field1.get(this) as View
                val p = popupView.layoutParams as WindowManager.LayoutParams
                p.windowAnimations = 0

                val field2 = WindowManager.LayoutParams::class.java.getDeclaredField("privateFlags")
                field2.isAccessible = true
                var flag = field2.getInt(p)
                flag = flag or 0x00000040
                field2.set(p, flag)

            } catch (e: NoSuchFieldException) {
                Log.e("WINDOW_PARAMS", "Could not disable window animations.", e)
            } catch (e: IllegalAccessException) {
                Log.e("WINDOW_PARAMS", "Could not disable window animations.", e)
            }
        }
    }
}