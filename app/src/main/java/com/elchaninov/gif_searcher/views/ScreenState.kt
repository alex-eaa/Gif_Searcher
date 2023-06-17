package com.elchaninov.gif_searcher.views

import android.content.Context
import android.content.res.Configuration
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.enum.Layout
import com.elchaninov.gif_searcher.enum.Theme
import com.elchaninov.gif_searcher.model.datasource.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenState @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settings: AppSettings
) {

    fun changeThemeMode(theme: Theme) {
        settings.nightTheme = theme
    }

    fun changeLayoutMode() {
        when (settings.layoutManager) {
            Layout.LINEAR -> settings.layoutManager = Layout.STAGGERED
            else -> settings.layoutManager = Layout.LINEAR
        }
    }

    fun getThemeMode(): Int = settings.nightTheme.value

    fun setIconsChangeLayoutItemsMenu(menu: Menu) {
        menu.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())
    }

    fun setThemeItemsMenu(menu: Menu) {
        when (settings.nightTheme) {
            Theme.DARK -> menu.findItem(R.id.theme_dark).isChecked = true
            Theme.LIGHT -> menu.findItem(R.id.theme_light).isChecked = true
            else -> menu.findItem(R.id.theme_auto).isChecked = true
        }
    }

    fun getItemLayoutForInflate(): Int = when (settings.layoutManager) {
        Layout.LINEAR -> R.layout.item_line_gif
        else -> R.layout.item_gif
    }

    fun getCategoriesLayoutManager(): RecyclerView.LayoutManager =
        StaggeredGridLayoutManager(
            getCategoriesSpanCount(),
            StaggeredGridLayoutManager.VERTICAL
        )

    fun getMyLayoutManager(): RecyclerView.LayoutManager =
        when (settings.layoutManager) {
            Layout.LINEAR -> LinearLayoutManager(context)
            else -> StaggeredGridLayoutManager(
                getSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )
        }

    fun getIconForChangeLayoutItemMenu(): Int =
        when (settings.layoutManager) {
            Layout.LINEAR -> R.drawable.ic_baseline_view_agenda_24
            else -> R.drawable.ic_baseline_dashboard_24
        }

    private fun getCategoriesSpanCount(): Int {
        return if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            CATEGORIES_SPAN_COUNT_LANDSCAPE
        else
            CATEGORIES_SPAN_COUNT_PORTRAIT
    }

    private fun getSpanCount(): Int =
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) SPAN_COUNT_LANDSCAPE
        else SPAN_COUNT_PORTRAIT

    companion object {
        const val SPAN_COUNT_PORTRAIT = 3
        const val SPAN_COUNT_LANDSCAPE = 5
        const val CATEGORIES_SPAN_COUNT_PORTRAIT = 2
        const val CATEGORIES_SPAN_COUNT_LANDSCAPE = 3
    }
}