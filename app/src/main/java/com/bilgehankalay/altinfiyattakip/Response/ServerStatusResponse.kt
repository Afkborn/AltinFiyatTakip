package com.bilgehankalay.altinfiyattakip.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerStatusResponse(
    @SerializedName("status") @Expose var status : Int
)
