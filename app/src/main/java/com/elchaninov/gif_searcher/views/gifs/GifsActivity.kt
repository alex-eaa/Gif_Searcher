package com.elchaninov.gif_searcher.views.gifs

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifsListActivityBinding
import com.elchaninov.gif_searcher.model.data.SearchQuery
import com.elchaninov.gif_searcher.utils.hide
import com.elchaninov.gif_searcher.utils.show
import com.elchaninov.gif_searcher.utils.showSnackbar
import com.elchaninov.gif_searcher.viewModel.GifsViewModel
import com.elchaninov.gif_searcher.views.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GifsActivity : BaseActivity<GifsViewModel>() {

    override val viewModel: GifsViewModel by viewModels()
    private lateinit var binding: GifsListActivityBinding
    private lateinit var gifsAdapter: GifsRxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifsListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchFab(binding.fabSearchContainer.fabSearch)
        initToolbar(binding.topAppBar)
        initRecyclerView()

        intent.getStringExtra(EXTRA_CATEGORIES)?.let {
            onSearch(it)
        }
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)
        supportActionBar?.title = getString(R.string.top)
    }

    private fun initRecyclerView() {
        gifsAdapter = GifsRxAdapter(
            itemLayoutForInflate = screenState.getItemLayoutForInflate(),
            onItemClick = { onItemClick(it) }
        )
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
                binding.progressContainer.progressLayout.show()
            }
            else -> {
                binding.progressContainer.progressLayout.hide()

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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.favorites)?.isVisible = viewModel.isFavoritesNotEmpty
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.findItem(R.id.action_change_layout)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
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
            else -> true
        }
    }

    override fun onSearch(searchWord: String) {
        viewModel.changeSearchQuery(SearchQuery.Search(searchWord))
        supportActionBar?.title = searchWord
    }

    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_1),
            actionText = getString(R.string.button_try_again),
            action = { gifsAdapter.retry() }
        )
    }

}