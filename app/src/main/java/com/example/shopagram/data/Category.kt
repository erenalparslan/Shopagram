package com.example.shopagram.data

sealed class Category(val category: String) {

    object Chair: Category("Chair")
    object Phone: Category("Phone")
    object Table: Category("Table")
    object Computer: Category("Computer")
    object Shoe: Category("Shoe")
}