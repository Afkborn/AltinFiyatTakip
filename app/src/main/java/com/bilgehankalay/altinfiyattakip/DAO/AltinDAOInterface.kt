package com.bilgehankalay.altinfiyattakip.DAO

import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import retrofit2.Call
import retrofit2.http.GET

interface AltinDAOInterface {
    @GET("/api/v1/altinlar")
    fun altinlariAl() : Call<AltinlarResponse>
}