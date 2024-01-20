package com.example.shopagram.fragments.shopping


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.shopagram.R
import com.example.shopagram.adapters.HomeViewpagerAdapter
import com.example.shopagram.databinding.FragmentHomeBinding
import com.example.shopagram.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val TAG="HomeFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        binding.viewpagerHome.currentItem=0
        val categoriesFragments = arrayListOf(
            MainCategoryFragment(),
            SweatshirtFragment(),
            ShoeFragment(),
            TableFragment(),
            PhoneFragment(),
            ComputerFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Anasayfa"
                1 -> tab.text = "Sweatshirt"
                2 -> tab.text = "Ayakkabı"
                3 -> tab.text = "Masa"
                4 -> tab.text = "Telefon"
                5 -> tab.text = "Bilgisayar"
            }
        }.attach()

    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()

    }




}