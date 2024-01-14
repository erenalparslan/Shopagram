package com.example.shopagram.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopagram.data.Product
import com.example.shopagram.databinding.FragmentSearchBinding
import com.example.shopagram.databinding.ProductRvItemBinding
import com.example.shopagram.helper.getProductPrice

class SearchAdapter: androidx.recyclerview.widget.ListAdapter<Product,SearchAdapter.SearchViewHolder>(FolderDiff) {

    inner class SearchViewHolder(private val binding: ProductRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            with(binding) {
                    val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                    tvNewPrice.text = "${String.format("%.2f", priceAfterOffer)} TL"

                    if (product.offerPercentage == null){

                        tvNewPrice.visibility = View.INVISIBLE
                    }else{
                        tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    Glide.with(itemView).load(product.images[0]).into(imgProduct)
                    tvPrice.text = "${product.price} TL"
                    tvName.text = product.name
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchViewHolder {
        return SearchViewHolder(ProductRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        val product = currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    object FolderDiff :DiffUtil.ItemCallback<Product>(){


        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem==newItem
        }

    }

    var onClick: ((Product) -> Unit)? = null
}