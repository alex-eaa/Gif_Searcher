package com.elchaninov.gif_searcher.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.enum.Theme
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.viewModel.BaseViewModel
import com.elchaninov.gif_searcher.views.favorites.FavoritesActivity
import com.elchaninov.gif_searcher.views.fullGif.FullGifActivity
import com.elchaninov.gif_searcher.views.gifs.GifsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import kotlinx.coroutines.launch

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(),
    SearchDialogFragment.OnSearchClickListener {

    @Inject
    lateinit var screenState: ScreenState
    abstract val viewModel: T
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(screenState.getThemeMode())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavoritesNotEmptyFlow.collect {
                    invalidateOptionsMenu()
                }
            }
        }
    }

    open fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        menu?.let { screenState.setIconsItemsMenu(it) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorites -> {
                startFavoriteActivity()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            R.id.theme_dark -> {
                screenState.changeThemeMode(Theme.DARK)
                this.recreate()
                true
            }
            R.id.theme_light -> {
                screenState.changeThemeMode(Theme.LIGHT)
                this.recreate()
                true
            }
            R.id.theme_auto -> {
                screenState.changeThemeMode(Theme.AUTO)
                this.recreate()
                true
            }
            else -> true
        }
    }

    protected fun initSearchFab(fab: FloatingActionButton) {
        fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun onSearch(searchWord: String) {
        searchView?.hideKeyboard()
        startSearchActivity(searchWord)
    }

    protected fun startSearchActivity(searchWord: String?) {
        val intent = Intent(this, GifsActivity::class.java)
        intent.putExtra(EXTRA_CATEGORIES, searchWord)
        startActivity(intent)
    }

    protected fun startFavoriteActivity() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }

    protected fun onItemClick(gif: Gif) {
        val intent = Intent(this, FullGifActivity::class.java)
        intent.putExtra(FullGifActivity.EXTRA_GIF, gif)
        startActivity(intent)
    }

    companion object {
        const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "CategoriesActivity_BOTTOM_SHEET_FRAGMENT"
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
    }
}