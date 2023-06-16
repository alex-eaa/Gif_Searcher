package com.elchaninov.gif_searcher.views.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.BuildConfig
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.model.data.LoadingState
import com.elchaninov.gif_searcher.model.data.userdata.TypedCategory
import com.elchaninov.gif_searcher.utils.hide
import com.elchaninov.gif_searcher.utils.show
import com.elchaninov.gif_searcher.utils.showSnackbar
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.views.BaseActivity
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainActivityBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchFab(binding.fabSearchContainer.fabSearch)
        initToolbar(binding.topAppBar)
        initRecyclerView()
        onBackPressedInit()
        if (BuildConfig.ALLOW_AD) initAdmob()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.updateData()
        }

        observerInitialization()
    }

    private fun observerInitialization() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isShowCollapseItemMenuFlow.collect {
                    invalidateOptionsMenu()
                }
            }
        }
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.topAppBar.logo =
            ContextCompat.getDrawable(this, R.drawable.poweredby_640px_black_horiztext)
    }

    private fun initRecyclerView() {
        categoriesAdapter = CategoriesAdapter(onItemClickListener = { onItemClick(it) })

        binding.recyclerView.apply {
            layoutManager = screenState.getCategoriesLayoutManager()
            adapter = categoriesAdapter
        }
        binding.recyclerView.addItemDecoration(
            CustomItemDecoration(resources.getDimensionPixelSize(R.dimen.item_decoration_space))
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.combinedLoadingStateFlow.collect { state ->
                    when (state) {
                        is LoadingState.Success -> {
                            binding.progressContainer.progressLayout.hide()
                            binding.swipeToRefresh.isRefreshing = false
                            updateAdapterData(state.data)
                        }
                        is LoadingState.Failure -> {
                            binding.progressContainer.progressLayout.hide()
                            binding.swipeToRefresh.isRefreshing = false
                            updateAdapterData(state.data ?: emptyList())
                            showError()
                        }
                        is LoadingState.Progress -> {
                            binding.progressContainer.progressLayout.show()
                            updateAdapterData(state.data ?: emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun updateAdapterData(newList: List<TypedCategory>) {
        val categoriesDiffUtilCallback =
            CategoriesDiffUtilCallback(categoriesAdapter.getItems(), newList)
        val categoriesDiffResult = DiffUtil.calculateDiff(categoriesDiffUtilCallback)
        categoriesAdapter.setItems(newList)
        categoriesDiffResult.dispatchUpdatesTo(categoriesAdapter)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.favorites)?.isVisible = viewModel.isFavoritesNotEmpty
        menu?.findItem(R.id.collapse_categories)?.isVisible =
            viewModel.isShowCollapseItemMenuFlow.value
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.collapse_categories -> {
                viewModel.collapseAll()
                true
            }
            else -> true
        }
    }

    private fun onItemClick(category: TypedCategory) {
        when (category) {
            is TypedCategory.Subcategory -> startSearchActivity(category.name)
            is TypedCategory.Custom.Trending -> startSearchActivity(null)
            is TypedCategory.Custom.Favorite -> startFavoriteActivity()
            is TypedCategory.Category -> viewModel.onClickCategory(category)
        }
    }

    private fun initAdmob() {
        MobileAds.initialize(this)
    }

    private fun onBackPressedInit() {
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.isShowCollapseItemMenuFlow.value) viewModel.collapseAll()
                    else finish()
                }
            })
    }
    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_3),
            actionText = getString(R.string.button_try_again),
            action = { viewModel.updateData() }
        )
    }

}