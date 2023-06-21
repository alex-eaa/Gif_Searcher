package com.elchaninov.gif_searcher.model.data

sealed class SearchQuery {
    data class Search(val query: String) : SearchQuery()
    object Top : SearchQuery()
}