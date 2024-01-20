package com.example.shopagram.fragments.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import com.example.shopagram.R
import com.example.shopagram.adapters.PostsAdapter
import com.example.shopagram.data.Address
import com.example.shopagram.data.Post
import com.example.shopagram.data.Product
import com.example.shopagram.util.Resource
import com.example.shopagram.databinding.FragmentSocialBinding
import com.example.shopagram.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialFragment :Fragment(R.layout.fragment_social) {

    val binding by lazy {
        FragmentSocialBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        PostsAdapter()
    }
    private val viewModel by viewModels<PostViewModel>()
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            viewModel.sharePost.collectLatest {
                when(it){
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                    }
                }
            }
        }
        with(binding) {
            postsRV.adapter=adapter
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        adapter.onClick = {
            product = it
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_socialFragment_to_productDetailsFragment,b)

        }



    }

    private fun refreshData() {
        // TODO: Yenileme işlemini gerçekleştirin (örneğin, yeni veri yükleyin)
        Toast.makeText(requireContext(),"Refreshing",Toast.LENGTH_SHORT).show()
        viewModel.fetchPosts()
        // Yenileme işlemi tamamlandığında SwipeRefreshLayout'ı durdurun
        binding.swipeRefreshLayout.isRefreshing = false
    }


}