package app.jhau.appopsmanager.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import app.jhau.appopsmanager.R

class IPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    Preference(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null, androidx.preference.R.attr.preferenceStyle, android.R.attr.preferenceStyle)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,androidx.preference.R.attr.preferenceStyle, android.R.attr.preferenceStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, android.R.attr.preferenceStyle)

    init {
        layoutResource = R.layout.preference_appops_item
    }

}