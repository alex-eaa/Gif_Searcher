package com.elchaninov.gif_searcher

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class MainActivity : AppCompatActivity(), GifAdapter.OnItemClickListener {

    private lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java).also {
            App.instance.component.inject(it)
        }
    }

    private lateinit var gifAdapter: GifAdapter
    private var isLinearLayoutManager = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initRecyclerView()

        viewModel.gifs.observe(this, { gifs ->
            gifAdapter.data = gifs
        })

        if (savedInstanceState == null) {
            viewModel.searchGifs()
        }
    }

    private fun initRecyclerView() {
        gifAdapter = GifAdapter(getItemLayoutForInflate(isLinearLayoutManager), this)
        binding.recyclerView.layoutManager = getLayoutManager(isLinearLayoutManager)
        binding.recyclerView.adapter = gifAdapter

        viewModel.gifs.value?.let { gifAdapter.data = it }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        menu?.let {
            it.findItem(R.id.action_change_layout)?.setIcon(getIconForChangeLayoutItemMenu())
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_change_layout) {
            isLinearLayoutManager = !isLinearLayoutManager
            item.setIcon(getIconForChangeLayoutItemMenu())
            initRecyclerView()
            true
        } else super.onOptionsItemSelected(item)
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