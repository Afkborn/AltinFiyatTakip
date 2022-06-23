package com.bilgehankalay.altinfiyattakip.DAO

import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.Response.PostAlisSatisResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AltinDAOInterface {
    @GET("/api/v1/altinlar")
    fun altinlariAlV1() : Call<AltinlarResponse>

    @GET("/api/v2/altinlar")
    fun altinlariAlV2() : Call<DegerliResponse>

    @GET("/api/v2/altinlar/{code}")
    fun altinAlV2(
        @Path("code")  code : String
    ) : Call<DegerliResponse>

    @POST("/api/v2/altinlar/{code}")
    fun dateAl(
        @Path("code") code : String,
        @Query("t1") t1 : String,
        @Query("t2") t2 : String
    ) : Call<PostAlisSatisResponse>



}