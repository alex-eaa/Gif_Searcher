package com.elchaninov.gif_searcher.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.ui.FullGifActivity
import com.elchaninov.gif_searcher.ui.FullGifActivity.Companion.EXTRA_GIF
import com.elchaninov.gif_searcher.ui.ScreenState
import com.elchaninov.gif_searcher.ui.enum.Theme
import com.elchaninov.gif_searcher.viewModel.FavoritesViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    @Inject
    lateinit var screenState: ScreenState

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: FavoritesViewModel

    private lateinit var binding: MainActivityBinding
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        setDefaultNightMode(screenState.getThemeMode())
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
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

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.favorites)

            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        menu?.findItem(R.id.collapse_categories)?.isVisible = false
        menu?.findItem(R.id.favorites)?.isVisible = false
        menu?.let { screenState.setIconsItemsMenu(it) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    private fun updateAdapterData(newList: List<Gif>) {
        val gifsDiffUtilCallback =
            GifsDiffUtilCallback(favoritesAdapter.getItems(), newList)
        val categoriesDiffResult = DiffUtil.calculateDiff(gifsDiffUtilCallback)
        favoritesAdapter.setItems(newList)
        categoriesDiffResult.dispatchUpdatesTo(favoritesAdapter)
    }

    private fun onItemClick(gif: Gif) {
        val intent = Intent(this, FullGifActivity::class.java)
        intent.putExtra(EXTRA_GIF, gif)
        startActivity(intent)
    }
}