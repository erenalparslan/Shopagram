package com.example.shopagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopagram.data.Product
import com.example.shopagram.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel@Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchAllProducts("")
    }

    fun fetchAllProducts(search: String) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
                val query = if (search.isNotBlank()) {
                    firestore.collection("Products")
                        .whereEqualTo("name", search)
                        .get()
                } else {
                    firestore.collection("Products").get()
                }

                query.addOnSuccessListener { result ->
                    val bestProducts = result.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProducts
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(bestProducts))
                    }
                    pagingInfo.bestProductsPage++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
            }

    }

    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBestProducts: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false
    )

}
