package com.elchaninov.gif_searcher.ui.gifs

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifsListActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.ui.BaseActivity
import com.elchaninov.gif_searcher.ui.FullGifActivity
import com.elchaninov.gif_searcher.ui.FullGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.ui.hide
import com.elchaninov.gif_searcher.ui.show
import com.elchaninov.gif_searcher.ui.showSnackbar
import com.elchaninov.gif_searcher.viewModel.GifsViewModel
import com.elchaninov.gif_searcher.viewModel.SearchQuery
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

    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_1),
            actionText = getString(R.string.button_try_again),
            action = { gifsAdapter.retry() }
        )
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

    private fun onItemClick(gif: Gif) {
        val intent = Intent(this, FullGifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }

    override fun onSearch(searchWord: String) {
        super.onSearch(searchWord)
        viewModel.changeSearchQuery(SearchQuery.Search(searchWord))
        supportActionBar?.title = searchWord
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}