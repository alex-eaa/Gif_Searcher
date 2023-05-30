package com.elchaninov.gif_searcher.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.BuildConfig
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.CategoriesActivityBinding
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.ui.Enum.Theme
import com.elchaninov.gif_searcher.viewModel.LoadingState
import com.elchaninov.gif_searcher.viewModel.CategoriesViewModel
import com.google.android.gms.ads.MobileAds
import javax.inject.Inject


class CategoriesActivity : AppCompatActivity(), CategoriesAdapter.OnItemClickListener,
    SearchDialogFragment.OnSearchClickListener {

    @Inject
    lateinit var screenState: ScreenState

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CategoriesViewModel

    private lateinit var binding: CategoriesActivityBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        setDefaultNightMode(screenState.getThemeMode())
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoriesViewModel::class.java]

        binding = CategoriesActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        initViews()
        if (BuildConfig.ALLOW_AD) initAdmob()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.updateData()
        }
    }

    private fun initRecyclerView() {
        categoriesAdapter = CategoriesAdapter(this)

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                getCategoriesSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = categoriesAdapter
        }

        viewModel.dataLiveData.observe(this) { state ->
            when (state) {
                is LoadingState.Success -> {
                    binding.progressContainer.progress.hide()
                    binding.swipeToRefresh.isRefreshing = false
                    categoriesAdapter.setItems(state.file)
                }
                is LoadingState.Failure -> {
                    binding.progressContainer.progress.hide()
                    binding.swipeToRefresh.isRefreshing = false
                    showError()
                }
                is LoadingState.Progress -> {
                    binding.progressContainer.progress.show()
                }
            }
        }
    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_1),
            actionText = getString(R.string.button_try_again),
            action = { viewModel.updateData() }
        )
    }

    private fun initToolbar() {
        binding.topAppBar.logo = ContextCompat.getDrawable(this, R.drawable.poweredby_640px_black_horiztext)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_change_layout)?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    override fun onItemClick(category: Category) {
        startSearchActivity(category.name)
    }

    override fun onSearch(searchWord: String) {
        searchView?.hideKeyboard()
        startSearchActivity(searchWord)
    }

    private fun startSearchActivity(searchWord: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_CATEGORIES, searchWord)
        startActivity(intent)
    }

    private fun initAdmob() {
        MobileAds.initialize(this)
    }

    private fun getCategoriesSpanCount(): Int {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            CATEGORIES_SPAN_COUNT_LANDSCAPE
        else
            CATEGORIES_SPAN_COUNT_PORTRAIT
    }

    companion object {
        const val CATEGORIES_SPAN_COUNT_PORTRAIT = 2
        const val CATEGORIES_SPAN_COUNT_LANDSCAPE = 3
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "CategoriesActivity_BOTTOM_SHEET_FRAGMENT"
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
    }
}