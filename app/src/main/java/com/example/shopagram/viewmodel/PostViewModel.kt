package com.example.shopagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopagram.data.Post
import com.example.shopagram.data.Product
import com.example.shopagram.firebase.FirebaseCommon
import com.example.shopagram.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
):ViewModel() {

    private val _sharePost = MutableStateFlow<Resource<List<Post>>>(Resource.Unspecified())
    val sharePost = _sharePost.asStateFlow()

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _sharePost.emit(Resource.Loading())
        }
        firestore.collection("sharedPosts").orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
                val posts = result.toObjects(Post::class.java)
                viewModelScope.launch {
                    _sharePost.emit(Resource.Success(posts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _sharePost.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}