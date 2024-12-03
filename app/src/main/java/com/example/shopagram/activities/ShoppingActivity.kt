package com.example.shopagram.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopagram.R
import com.example.shopagram.databinding.ActivityShoppingBinding
import com.example.shopagram.util.Resource
import com.example.shopagram.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ -> }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            val count = it.data?.size ?: 0
                            val bottomNavigation =
                                findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                                number = count
                                backgroundColor =
                                    ContextCompat.getColor(this@ShoppingActivity, R.color.g_blue)
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }

    }

}