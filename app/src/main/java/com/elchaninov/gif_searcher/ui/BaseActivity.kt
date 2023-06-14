package com.elchaninov.gif_searcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
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

    open fun initSearchFab(fab: FloatingActionButton) {
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