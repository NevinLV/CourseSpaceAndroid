package com.example.coursespace

import com.example.coursespace.api.Courses
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("courses/?format=json")
    fun getCorses(): Call<Courses>
}