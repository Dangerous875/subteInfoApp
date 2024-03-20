package com.example.subwaystatus.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getSubwayResponse(@Url url:String):Response<SubwayResponse>

}