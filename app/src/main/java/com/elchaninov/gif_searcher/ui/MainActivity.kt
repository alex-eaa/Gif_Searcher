package com.elchaninov.gif_searcher.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
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
import com.elchaninov.gif_searcher.ui.Enum.Theme
import com.elchaninov.gif_searcher.ui.ShowingGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import javax.inject.Inject

class MainActivity : AppCompatActivity(), GifsRxAdapter.OnItemClickListener {

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
        viewModel = viewModelFactory.create(MainViewModel::class.java)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
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
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = getString(R.string.top)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        binding.topAppBar.setNavigationIcon(R.drawable.ic_sharp_grade_24)

        menu?.let { it ->
            screenState.setIconsItemsMenu(it)

            searchView = it.findItem(R.id.action_search).actionView as SearchView?
            searchView?.imeOptions = EditorInfo.IME_ACTION_SEARCH
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView?.hideKeyboard()
                    query?.let { queryString ->
                        viewModel.changeSearchQuery(SearchQuery.Search(queryString))
                        supportActionBar?.title = queryString
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_layout -> {
                screenState.changeLayoutMode()
                this.recreate()
                true
            }
            android.R.id.home -> {
                searchView?.onActionViewCollapsed()
                viewModel.changeSearchQuery(SearchQuery.Top)
                supportActionBar?.title = getString(R.string.top)
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


    companion object {
        const val SPAN_COUNT_PORTRAIT = 3
        const val SPAN_COUNT_LANDSCAPE = 5
    }
}