package com.elchaninov.gif_searcher.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.ui.CategoriesActivity.Companion.EXTRA_CATEGORIES
import com.elchaninov.gif_searcher.ui.enum.Theme
import com.elchaninov.gif_searcher.ui.ShowingGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import javax.inject.Inject

class MainActivity : AppCompatActivity(), GifsRxAdapter.OnItemClickListener,
    SearchDialogFragment.OnSearchClickListener {

    @Inject
    lateinit var screenState: ScreenState

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    private lateinit var binding: MainActivityBinding
    private lateinit var gifsAdapter: GifsRxAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        setDefaultNightMode(screenState.getThemeMode())
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        initViews()

        intent.getStringExtra(EXTRA_CATEGORIES)?.let {
            onSearch(it)
        }
    }

    private fun initRecyclerView() {
        gifsAdapter =
            GifsRxAdapter(screenState.getItemLayoutForInflate(), this)
        gifsAdapter.addLoadStateListener { combinedLoadStates ->
            processingPreloadStates(combinedLoadStates)
        }

        binding.recyclerView.apply {
            layoutManager = screenState.getMyLayoutManager()
            adapter = gifsAdapter
            adapter = gifsAdapter.withLoadStateHeaderAndFooter(
                header = GifsLoadStateAdapter { gifsAdapter.retry() },
                footer = GifsLoadStateAdapter { gifsAdapter.retry() }
            )
        }
        viewModel.pagingDataLiveData.observe(this) { observable ->
            gifsAdapter.submitData(lifecycle, observable)
        }
    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    private fun processingPreloadStates(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Loading -> {
                binding.progressContainer.progress.show()
            }
            else -> {
                binding.progressContainer.progress.hide()

                val error = when (loadState.refresh) {
                    is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_1),
            actionText = getString(R.string.button_try_again),
            action = { gifsAdapter.retry() }
        )
    }

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply{
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.top)

            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        menu?.let { screenState.setIconsItemsMenu(it) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_layout -> {
                screenState.changeLayoutMode()
                item.setIcon(screenState.getIconForChangeLayoutItemMenu())

                val scrollToPosition = if (gifsAdapter.direction) gifsAdapter.lastPosition
                else gifsAdapter.lastPosition - binding.recyclerView.childCount

                initRecyclerView()
                binding.recyclerView.scrollToPosition(scrollToPosition)
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

    override fun onItemClick(gif: Gif) {
        val intent = Intent(this, ShowingGifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }

    override fun onSearch(searchWord: String) {
        searchView?.hideKeyboard()
        viewModel.changeSearchQuery(SearchQuery.Search(searchWord))
        supportActionBar?.title = searchWord
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}