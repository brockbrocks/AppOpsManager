package app.jhau.appopsmanager.ui.app

import android.graphics.drawable.Drawable

data class AppUiState(
    val apps: List<AppItemUiState> = listOf()
)

data class AppItemUiState(
    val appName: String = "",
    val packageName: String = "",
    val icon: Drawable
)
