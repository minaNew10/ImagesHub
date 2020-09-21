package com.example.imageshub.api

import com.example.imageshub.data.UnsplashPhoto

data class UnsplashResponse (
    val results:List<UnsplashPhoto>
)