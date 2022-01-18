package com.elchaninov.gif_searcher.ui.main.LoadState

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.LoadStateViewBinding

class GifsLoadStateViewHolder(
    private val binding: LoadStateViewBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.loadStateRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.loadStateErrorMessage.text = loadState.error.localizedMessage
        }
        binding.loadStateProgress.isVisible = loadState is LoadState.Loading
        binding.loadStateRetry.isVisible = loadState is LoadState.Error
        binding.loadStateErrorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): GifsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_view, parent, false)
            val binding = LoadStateViewBinding.bind(view)
            return GifsLoadStateViewHolder(binding, retry)
        }
    }

}