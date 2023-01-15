package app.jhau.appopsmanager.util

import android.content.res.Resources
import android.util.TypedValue

fun Int.px(): Float {
    return dp2px(this.toFloat())
}

fun Float.px(): Float {
    return dp2px(this)
}

private fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    )
}

