package app.jhau.appopsmanager.ui

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.databinding.BindingAdapter

@BindingAdapter("android:clipRoundCornerRadius")
fun View.setClipRoundCornerRadius(radius: Float) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            clipToOutline = true
            outline?.setRoundRect(0, 0, width, height,radius)
        }
    }
}