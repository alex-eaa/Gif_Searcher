package com.elchaninov.gif_searcher.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.ui.enum.Theme
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

abstract class BaseActivity<T : ViewModel> : AppCompatActivity(),
    SearchDialogFragment.OnSearchClickListener {

    @Inject
    lateinit var screenState: ScreenState
    abstract val viewModel: T
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(screenState.getThemeMode())
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
    }

    companion object {
        const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "CategoriesActivity_BOTTOM_SHEET_FRAGMENT"
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
    }
}