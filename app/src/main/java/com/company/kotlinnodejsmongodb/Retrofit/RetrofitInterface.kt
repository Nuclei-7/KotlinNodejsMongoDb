package com.company.kotlinnodejsmongodb.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitInterface {
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String?, String?>?): Call<LoginResult?>?

    @POST("/register")
    fun executeSignup(@Body map: HashMap<String?, String?>?): Call<Void?>?
}

