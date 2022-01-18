package com.elchaninov.gif_searcher.ui.main.LoadState

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class GifsLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<GifsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: GifsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): GifsLoadStateViewHolder {
        return GifsLoadStateViewHolder.create(parent, retry)
    }
}