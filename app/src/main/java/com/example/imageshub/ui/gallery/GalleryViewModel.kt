package com.example.imageshub.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imageshub.data.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel(){
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    //observed in a fragment
 //   val photos = repository.getSearchResults()
    //the lambda get executed whenever the value of currentQuery live data get changed
    val photos = currentQuery.switchMap {
        //it is the new value of currentQuery
        //we cached the data to avoid crashing when rotating the screen because we can't load from the same paging data twice
        repository.getSearchResults(it).cachedIn(viewModelScope)
    }
    fun searchPhotos(query: String){
        currentQuery.value = query
    }
    companion object{
        private const val DEFAULT_QUERY = "cats";
    }
}