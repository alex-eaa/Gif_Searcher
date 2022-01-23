package com.elchaninov.gif_searcher.ui

import android.content.Context
import android.content.res.Configuration
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.Settings
import com.elchaninov.gif_searcher.ui.Enum.Layout
import com.elchaninov.gif_searcher.ui.Enum.Theme
import javax.inject.Inject

class ScreenState @Inject constructor(
    private val context: Context,
    private val settings: Settings
) {

    fun changeThemeMode(theme: Theme) {
        settings.nightTheme = theme
    }

    fun changeLayoutMode() {
        when (settings.layoutManager) {
            Layout.LINEAR -> settings.layoutManager = Layout.STAGGERED
            Layout.STAGGERED -> settings.layoutManager = Layout.GRID
            else -> settings.layoutManager = Layout.LINEAR
        }
    }

    fun getThemeMode(): Int = when (settings.nightTheme) {
        Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    fun setIconsItemsMenu(menu: Menu) {
        menu.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())

        when (settings.nightTheme) {
            Theme.DARK -> menu.findItem(R.id.theme_dark).isChecked = true
            Theme.LIGHT -> menu.findItem(R.id.theme_light).isChecked = true
            else -> menu.findItem(R.id.theme_auto).isChecked = true
        }
    }

    fun getItemLayoutForInflate(): Int = when (settings.layoutManager) {
        Layout.GRID, Layout.STAGGERED -> R.layout.item_gif
        else -> R.layout.item_line_gif
    }

    fun getMyLayoutManager(): RecyclerView.LayoutManager =
        when (settings.layoutManager) {
            Layout.LINEAR -> LinearLayoutManager(context)
            Layout.GRID -> GridLayoutManager(context, getSpanCount())
            else -> StaggeredGridLayoutManager(
                getSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )
        }

    private fun getIconForChangeLayoutItemMenu(): Int =
        when (settings.layoutManager) {
            Layout.LINEAR -> R.drawable.ic_baseline_dashboard_24
            Layout.GRID -> R.drawable.ic_baseline_view_agenda_24
            else -> R.drawable.ic_sharp_grid_view_24
        }

    private fun getSpanCount(): Int =
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) MainActivity.SPAN_COUNT_LANDSCAPE
        else MainActivity.SPAN_COUNT_PORTRAIT
}