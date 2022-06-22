package com.bilgehankalay.altinfiyattakip.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Altin(
    @SerializedName("id") var id : Int,
    @SerializedName("dataFlag") @Expose var dataFlag: String,
    @SerializedName("altinAdi") @Expose var altinAdi : String,
    @SerializedName("aciklama") @Expose var aciklama : String,
    @SerializedName("gramaj") @Expose var gramaj : Float,
    @SerializedName("saflik") @Expose var saflik : Float,
    @SerializedName("ayar") @Expose var ayar : String,
    @SerializedName("alisFiyati") @Expose var alisFiyati : Float,
    @SerializedName("satisFiyati") @Expose var satisFiyati : Float,
    @SerializedName("tarih") @Expose var tarih : Float
    ) : Serializable

