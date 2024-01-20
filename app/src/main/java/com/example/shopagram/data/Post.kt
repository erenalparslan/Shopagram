package com.example.shopagram.data

import java.util.Date

data class Post(
    val user: User? = null,
    val product: Product? = null,
    val date: Date?=null
) {
}