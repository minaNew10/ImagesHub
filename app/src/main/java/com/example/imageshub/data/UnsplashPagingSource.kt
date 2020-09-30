package com.example.imageshub.data

import androidx.paging.PagingSource
import com.example.imageshub.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

//this constant is not related to the class so we didn't put it into a companion object
private const val UNSPLASH_STARTING_PAGE_INDEX =1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int,UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        //params.key will be null for the first page we are loading so we have to declare
        //which page we want to load in this case
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query,position,params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if(position == UNSPLASH_STARTING_PAGE_INDEX) null else position -1,
                nextKey = if(photos.isEmpty() )null else position+ 1
            )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }

}