package com.bilgehankalay.altinfiyattakip.Response


import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DegerliResponse(
    @SerializedName("status") @Expose var status : Int,

    @SerializedName("data") @Expose var altinlar : List<Degerli>
)
