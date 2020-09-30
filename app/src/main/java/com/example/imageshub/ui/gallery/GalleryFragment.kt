package com.example.imageshub.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            gallery_recycler_view.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnSplashPhotoLoadStateAdapter{adapter.retry()},
                footer = UnSplashPhotoLoadStateAdapter{adapter.retry()}


            )
        }
        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}