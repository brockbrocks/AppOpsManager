package app.jhau.appopsmanager.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.dateStr(pattern: String, locale: Locale = Locale.getDefault()): String {
    if (this <= 0) return ""
    return SimpleDateFormat(pattern, locale).format(this)
}