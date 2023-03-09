package app.jhau.appopsmanager.ui.appperms

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import app.jhau.appopsmanager.util.px

class PermDetailAdapter(ctx: Context, private val stringArray: Array<String>) :
    ArrayAdapter<String>(ctx, 0, stringArray) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) parent.setPadding(0, 30.px().toInt(), 0, 30.px().toInt())
        val view = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(30.px().toInt(), 2.px().toInt(), 30.px().toInt(), 2.px().toInt())
            text = stringArray[position]
        }
        return view
    }
}