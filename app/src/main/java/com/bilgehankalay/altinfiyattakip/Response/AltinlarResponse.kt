package com.bilgehankalay.altinfiyattakip.Response

import com.bilgehankalay.altinfiyattakip.Model.Altin
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AltinlarResponse(
    @SerializedName("status") @Expose var status : Int,
    @SerializedName("altinlar") @Expose var altinlar : List<Altin>
    )
