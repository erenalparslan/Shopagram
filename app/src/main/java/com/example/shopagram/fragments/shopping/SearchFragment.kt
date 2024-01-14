package com.example.shopagram.fragments.shopping

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopagram.R
import com.example.shopagram.adapters.SearchAdapter
import com.example.shopagram.databinding.FragmentSearchBinding
import com.example.shopagram.util.Resource
import com.example.shopagram.viewmodel.MainCategoryViewModel
import com.example.shopagram.viewmodel.SearchFragmentViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {

    private lateinit var binding :FragmentSearchBinding
    private val adapter by lazy {
        SearchAdapter()
    }
    private val viewModel by viewModels<SearchFragmentViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager= GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.searchRw.adapter=adapter
        binding.searchRw.layoutManager=layoutManager
        var searchText=""

        val watcher=object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("cipsi", "afterTextChanged: $p0 ")
                searchText=p0.toString()

            }
        }


        binding.searchEt.addTextChangedListener(watcher)
        binding.searchButton.setOnClickListener{
            viewModel.fetchAllProducts(searchText)
        }
        adapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment,b)
        }



        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                        binding.bestProductsProgressbar.visibility = View.GONE


                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.bestProductsProgressbar.visibility = View.GONE

                    }
                    else -> Unit
                }
            }
        }


    }


}