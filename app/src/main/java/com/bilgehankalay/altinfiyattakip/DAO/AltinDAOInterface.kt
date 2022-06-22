package com.bilgehankalay.altinfiyattakip.DAO

import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import retrofit2.Call
import retrofit2.http.GET

interface AltinDAOInterface {
    @GET("/api/v1/altinlar")
    fun altinlariAlV1() : Call<AltinlarResponse>

    @GET("/api/v2/altinlar")
    fun altinlariAlV2() : Call<DegerliResponse>
}