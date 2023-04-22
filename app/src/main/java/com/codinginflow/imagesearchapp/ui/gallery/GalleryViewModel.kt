package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val currentSubmittedQuery = state.getLiveData(CURRENT_SUBMITTED_QUERY_KEY, DEFAULT_QUERY)
    val currentEnteredQuery = state.getLiveData(CURRENT_ENTERED_QUERY_KEY, "")

    val photos = currentSubmittedQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentSubmittedQuery.value = query
    }

    fun onQueryTextChange(newQueryText: String) {
        currentEnteredQuery.value = newQueryText
    }

    companion object {
        private const val CURRENT_SUBMITTED_QUERY_KEY = "current_submitted_query"
        private const val CURRENT_ENTERED_QUERY_KEY = "current_entered_query"
        private const val DEFAULT_QUERY = "cats"
    }
}