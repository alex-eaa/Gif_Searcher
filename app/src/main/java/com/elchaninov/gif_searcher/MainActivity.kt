package com.elchaninov.gif_searcher

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.data.Gif
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.ui.gif.GifActivity
import com.elchaninov.gif_searcher.ui.gif.GifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.ui.main.GifAdapter
import com.elchaninov.gif_searcher.ui.main.MainViewModel
import com.elchaninov.gif_searcher.viewModel.AppState

class MainActivity : AppCompatActivity(), GifAdapter.OnItemClickListener {

    private lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java).also {
            App.instance.component.inject(it)
        }
    }

    private lateinit var gifAdapter: GifAdapter
    private var isLinearLayoutManager = true
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initRecyclerView()

        viewModel.appState.observe(this, { appState ->
            renderData(appState)
        })

        if (savedInstanceState == null) viewModel.fetchGifs()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                gifAdapter.data = appState.data
                binding.progressContainer.progress.hide()
                binding.errorContainer.error.hide()
            }
            is AppState.Loading -> {
                binding.errorContainer.error.hide()
                binding.progressContainer.progress.show()
            }
            is AppState.Error -> {
                binding.errorContainer.errorMsg.text = appState.message
                binding.errorContainer.tryAgainBtn.setOnClickListener { viewModel.tryAgain() }
                binding.errorContainer.error.show()
                binding.progressContainer.progress.hide()
            }
        }
    }

    private fun initRecyclerView() {
        gifAdapter = GifAdapter(getItemLayoutForInflate(isLinearLayoutManager), this)
        binding.recyclerView.layoutManager = getLayoutManager(isLinearLayoutManager)
        binding.recyclerView.adapter = gifAdapter

        viewModel.appState.value?.let { appState ->
            if (appState is AppState.Success) gifAdapter.data = appState.data
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "В тренде"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)
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
                    if (!query.isNullOrBlank()) {
                        viewModel.fetchGifs(query)
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
                isLinearLayoutManager = !isLinearLayoutManager
                item.setIcon(getIconForChangeLayoutItemMenu())
                initRecyclerView()
                true
            }
            android.R.id.home -> {
                searchView?.onActionViewCollapsed()
                viewModel.fetchGifs()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getIconForChangeLayoutItemMenu(): Int =
        if (isLinearLayoutManager) R.drawable.ic_baseline_view_agenda_24
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

    companion object {
        const val SPAN_COUNT_PORTRAIT = 3
        const val SPAN_COUNT_LANDSCAPE = 5
    }

    override fun onItemClick(gif: Gif) {
        val intent = Intent(this, GifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }
}