package com.example.imageshub.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.imageshub.R
import com.example.imageshub.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery){
    //because we added @AndroidEntry point Annotation this view Model will be injected with dagger
    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter()

        binding.apply {
            gallery_recycler_view.setHasFixedSize(true)
            //this line is to prevent the view of last elements when searching for new items
            galleryRecyclerView.itemAnimator = null
            gallery_recycler_view.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnSplashPhotoLoadStateAdapter{adapter.retry()},
                footer = UnSplashPhotoLoadStateAdapter{adapter.retry()}
            )
            btnRetry.setOnClickListener{
                adapter.retry()
            }
        }
        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        }
        adapter.addLoadStateListener {
            loadstate ->
                binding.apply {
                    progressBarGalleryRecyclerView.isVisible = loadstate.source.refresh is LoadState.Loading
                    galleryRecyclerView.isVisible = loadstate.source.refresh is LoadState.NotLoading
                    btnRetry.isVisible = loadstate.source.refresh is LoadState.Error
                    txtvError.isVisible = loadstate.source.refresh is LoadState.Error
                    if(loadstate.source.refresh is LoadState.NotLoading &&
                            // This parameter informs Pager if it should continue to make requests
                        // for additional data in this direction
                        // or if it should halt as the end of the dataset has been reached.
                            loadstate.append.endOfPaginationReached && adapter.itemCount < 1){
                        galleryRecyclerView.isVisible =false
                        txtvError.isVisible = true
                    }else{
                        txtvError.isVisible = false
                    }
                }

        }
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery,menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    binding.galleryRecyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}