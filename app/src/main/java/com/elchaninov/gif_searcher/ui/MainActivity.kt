package com.elchaninov.gif_searcher.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.ui.ShowingGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), GifsRxAdapter.OnItemClickListener {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var factory: MainViewModel.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private val disposable = CompositeDisposable()
    private lateinit var gifsAdapter: GifsRxAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        gifsAdapter =
            GifsRxAdapter(getItemLayoutForInflate(viewModel.isLinearLayoutManager), this)
        gifsAdapter.addLoadStateListener { combinedLoadStates ->
            processingPreloadStates(combinedLoadStates)
        }

        viewModel.pagingDataLiveData.observe(this) { observable ->
            disposable.add(
                observable.subscribe { gifsAdapter.submitData(lifecycle, it) })
        }

        binding.recyclerView.apply {
            layoutManager = getLayoutManager(viewModel.isLinearLayoutManager)
            adapter = gifsAdapter
            adapter = gifsAdapter.withLoadStateHeaderAndFooter(
                header = GifsLoadStateAdapter { gifsAdapter.retry() },
                footer = GifsLoadStateAdapter { gifsAdapter.retry() }
            )
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
        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_home_24)

        menu?.let {
            it.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())

            searchView = it.findItem(R.id.action_search).actionView as SearchView?
            searchView?.imeOptions = EditorInfo.IME_ACTION_SEARCH
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView?.hideKeyboard()
                    query?.let {
                        viewModel.changeSearchQuery(SearchQuery.Search(it))
                        supportActionBar?.title = it
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_layout -> {
                viewModel.changeLinearLayoutManager()
                initRecyclerView()
                item.setIcon(getIconForChangeLayoutItemMenu())
                true
            }
            android.R.id.home -> {
                searchView?.onActionViewCollapsed()
                viewModel.changeSearchQuery(SearchQuery.Top)
                supportActionBar?.title = getString(R.string.top)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getIconForChangeLayoutItemMenu(): Int =
        if (viewModel.isLinearLayoutManager) R.drawable.ic_baseline_dashboard_24
        else R.drawable.ic_baseline_view_agenda_24

    private fun getItemLayoutForInflate(isLinearLayoutManager: Boolean): Int =
        when (isLinearLayoutManager) {
            true -> R.layout.item_line_gif
            false -> R.layout.item_gif
        }

    private fun getLayoutManager(isLinearLayoutManager: Boolean): RecyclerView.LayoutManager =
        when (isLinearLayoutManager) {
            true -> LinearLayoutManager(this)
            false -> StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL)
        }

    private fun getSpanCount(): Int =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) SPAN_COUNT_LANDSCAPE
        else SPAN_COUNT_PORTRAIT

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
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