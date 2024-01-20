package com.example.shopagram.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.shopagram.data.Post
import com.example.shopagram.data.Product
import com.example.shopagram.databinding.PostItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostsAdapter : ListAdapter<Post, PostsAdapter.PostVH>(DiffUtil) {


    inner class PostVH(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            with(binding) {
                val imagePath = post.product?.images?.first()
                val profileImage = post.user?.imagePath
                val dateFromFirestore =formatDateTime(post.date!!)
                Glide.with(binding.root).load(imagePath)
                    .into(binding.postIV)
                if (profileImage != "") {
                    Glide.with(itemView).load(profileImage)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(binding.profileImage)
                }
                binding.date.text=dateFromFirestore
                binding.postOwnerTv.text = " ${post.user?.firstName} ${post.user?.lastName}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.PostVH = PostVH(
        PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: PostsAdapter.PostVH, position: Int) {
        holder.bind(currentList[position])
        val product = currentList[position].product
        holder.binding.postIV.setOnClickListener {
            onClick?.invoke(product!!)
        }
    }

    fun formatDateTime(date: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }


    object DiffUtil : ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }


    }

    var onClick: ((Product) -> Unit)? = null
}