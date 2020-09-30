package com.example.imageshub.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imageshub.databinding.UnsplashPhotoLoadStateFooterBinding

class UnSplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnSplashPhotoLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context),parent,false)


        return LoadStateViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
    //we have to declare the class as inner to be able to access the surrounding class memebers
    inner class LoadStateViewHolder(private val binding:UnsplashPhotoLoadStateFooterBinding):
            RecyclerView.ViewHolder(binding.root){
        init {
            //This is a good place to set a clickListener as onBindViewHolder will be repeated for every view
            //but this will be called only few times according to the number of layout views
            binding.btnRetry.setOnClickListener{
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState){
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                txtvError.isVisible = loadState !is LoadState.Loading
            }
        }

    }
}