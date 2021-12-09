package com.example.coursespace.api

data class CoursesItem(
    val author_id: Int,
    val category_id: Int,
    val date: String,
    val description: String,
    val title: String
)