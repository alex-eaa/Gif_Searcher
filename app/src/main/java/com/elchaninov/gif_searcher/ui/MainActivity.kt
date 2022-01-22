package com.elchaninov.gif_searcher.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.Settings
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.ui.Enum.Layout
import com.elchaninov.gif_searcher.ui.Enum.Theme
import com.elchaninov.gif_searcher.ui.ShowingGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import javax.inject.Inject

class MainActivity : AppCompatActivity(), GifsRxAdapter.OnItemClickListener {

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    private lateinit var binding: MainActivityBinding
    private lateinit var gifsAdapter: GifsRxAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        setTheme()
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(MainViewModel::class.java)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        gifsAdapter =
            GifsRxAdapter(getItemLayoutForInflate(), this)
        gifsAdapter.addLoadStateListener { combinedLoadStates ->
            processingPreloadStates(combinedLoadStates)
        }

        binding.recyclerView.apply {
            layoutManager = getMyLayoutManager()
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
            setIconsItemsMenu(it)

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
                when (settings.layoutManager) {
                    Layout.LINEAR -> settings.layoutManager = Layout.STAGGERED
                    Layout.STAGGERED -> settings.layoutManager = Layout.GRID
                    else -> settings.layoutManager = Layout.LINEAR
                }
                initRecyclerView()
                this.recreate()
                item.setIcon(getIconForChangeLayoutItemMenu())
                true
            }
            android.R.id.home -> {
                searchView?.onActionViewCollapsed()
                viewModel.changeSearchQuery(SearchQuery.Top)
                supportActionBar?.title = getString(R.string.top)
                true
            }
            R.id.theme_dark -> {
                settings.nightTheme = Theme.DARK
                this.recreate()
                true
            }
            R.id.theme_light -> {
                settings.nightTheme = Theme.LIGHT
                this.recreate()
                true
            }
            R.id.theme_auto -> {
                settings.nightTheme = Theme.AUTO
                this.recreate()
                true
            }
            else -> true
        }
    }

    private fun setIconsItemsMenu(menu: Menu) {
        menu.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())

        when (settings.nightTheme) {
            Theme.DARK -> menu.findItem(R.id.theme_dark).isChecked = true
            Theme.LIGHT -> menu.findItem(R.id.theme_light).isChecked = true
            else -> menu.findItem(R.id.theme_auto).isChecked = true
        }
    }

    private fun getIconForChangeLayoutItemMenu(): Int =
        when (settings.layoutManager) {
            Layout.LINEAR -> R.drawable.ic_baseline_dashboard_24
            Layout.GRID -> R.drawable.ic_baseline_view_agenda_24
            else -> R.drawable.ic_sharp_grid_view_24
        }

    private fun getItemLayoutForInflate(): Int = when (settings.layoutManager) {
        Layout.GRID, Layout.STAGGERED -> R.layout.item_gif
        else -> R.layout.item_line_gif
    }

    private fun getMyLayoutManager(): RecyclerView.LayoutManager =
        when (settings.layoutManager) {
            Layout.LINEAR -> LinearLayoutManager(this)
            Layout.GRID -> GridLayoutManager(this, getSpanCount())
            else -> StaggeredGridLayoutManager(
                getSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )
        }

    private fun getSpanCount(): Int =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) SPAN_COUNT_LANDSCAPE
        else SPAN_COUNT_PORTRAIT

    override fun onItemClick(gif: Gif) {
        val intent = Intent(this, ShowingGifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }

    private fun setTheme() {
        when (settings.nightTheme) {
            Theme.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
            Theme.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
            else -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    companion object {
        const val SPAN_COUNT_PORTRAIT = 3
        const val SPAN_COUNT_LANDSCAPE = 5
    }
}