package com.elchaninov.gif_searcher.views.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifsListActivityBinding
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.viewModel.FavoritesViewModel
import com.elchaninov.gif_searcher.views.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesActivity : BaseActivity<FavoritesViewModel>() {

    override val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: GifsListActivityBinding
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifsListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchFab(binding.fabSearchContainer.fabSearch)
        initToolbar(binding.topAppBar)
        initRecyclerView()
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)
        supportActionBar?.title = getString(R.string.favorites)
    }

    private fun initRecyclerView() {
        favoritesAdapter = FavoritesAdapter(
            itemLayoutForInflate = screenState.getItemLayoutForInflate(),
            onItemClickListener = { onItemClick(it) }
        )

        binding.recyclerView.apply {
            layoutManager = screenState.getMyLayoutManager()
            adapter = favoritesAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritesFlow.collect {
                    updateAdapterData(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.change_layout_menu, menu)
        menu?.let { screenState.setIconsChangeLayoutItemsMenu(it) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.action_change_layout -> {
                screenState.changeLayoutMode()
                item.setIcon(screenState.getIconForChangeLayoutItemMenu())

                val scrollToPosition = if (favoritesAdapter.direction) favoritesAdapter.lastPosition
                else favoritesAdapter.lastPosition - binding.recyclerView.childCount

                initRecyclerView()
                binding.recyclerView.scrollToPosition(scrollToPosition)
                true
            }
            else -> true
        }
    }

    private fun updateAdapterData(newList: List<Gif>) {
        val gifsDiffUtilCallback =
            GifsDiffUtilCallback(favoritesAdapter.getItems(), newList)
        val categoriesDiffResult = DiffUtil.calculateDiff(gifsDiffUtilCallback)
        favoritesAdapter.setItems(newList)
        categoriesDiffResult.dispatchUpdatesTo(favoritesAdapter)
    }
}