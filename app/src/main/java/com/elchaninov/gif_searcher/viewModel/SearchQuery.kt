package com.elchaninov.gif_searcher.viewModel

sealed class SearchQuery {
    data class Search(val query: String) : SearchQuery()
    object Top : SearchQuery()
    object Empty : SearchQuery()
}