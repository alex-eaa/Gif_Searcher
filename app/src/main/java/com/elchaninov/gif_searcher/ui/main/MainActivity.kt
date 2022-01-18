package com.elchaninov.gif_searcher.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.*
import com.elchaninov.gif_searcher.data.Gif
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.ui.gif.GifActivity
import com.elchaninov.gif_searcher.ui.gif.GifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.ui.main.LoadState.GifsLoadStateAdapter
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var factory: MainViewModel.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private val mDisposable = CompositeDisposable()
    private lateinit var adapter: GifsRxAdapter
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
        val isLinearLayoutManager = viewModel.isLinearLayoutManager
        adapter = GifsRxAdapter(getItemLayoutForInflate(isLinearLayoutManager), this)
        binding.recyclerView.layoutManager = getLayoutManager(isLinearLayoutManager)

        binding.recyclerView.adapter = adapter

        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GifsLoadStateAdapter { adapter.retry() },
            footer = GifsLoadStateAdapter { adapter.retry() }
        )

        mDisposable.add(viewModel.getGifs().subscribe {
            adapter.submitData(lifecycle, it)
        })

        adapter.addLoadStateListener { combinedLoadStates ->
            processingPreloadStates(combinedLoadStates)
        }
    }

    private fun processingPreloadStates(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Loading -> {
                binding.errorContainer.error.hide()
                binding.progressContainer.progress.show()
            }
            else -> {
                binding.progressContainer.progress.hide()

                val error = when {
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    binding.errorContainer.errorMsg.text = it.error.message
                    binding.errorContainer.error.show()
                    binding.errorContainer.tryAgainBtn.setOnClickListener {
                        adapter.retry()
                        binding.errorContainer.error.hide()
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Топ"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_home_24)

        menu?.let {
            it.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())

            searchView = menu.findItem(R.id.action_search).actionView as SearchView?
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView?.hideKeyboard()
                    adapter.retry()
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
//                viewModel.fetchGifs()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getIconForChangeLayoutItemMenu(): Int =
        if (viewModel.isLinearLayoutManager) R.drawable.ic_baseline_view_agenda_24
        else R.drawable.ic_baseline_grid_view_24

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
        mDisposable.dispose()
        super.onDestroy()
    }

    override fun onItemClick(gif: Gif) {
        val intent = Intent(this, GifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }

    companion object {
        const val SPAN_COUNT_PORTRAIT = 3
        const val SPAN_COUNT_LANDSCAPE = 5
    }
}