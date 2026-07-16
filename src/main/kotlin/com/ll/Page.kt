package com.ll

class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int
)