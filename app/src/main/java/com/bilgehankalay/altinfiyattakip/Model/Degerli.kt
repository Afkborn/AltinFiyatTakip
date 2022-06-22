package com.bilgehankalay.altinfiyattakip.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Degerli(
    @SerializedName("code") @Expose var code : String,
    @SerializedName("alis") @Expose var alis : Float,
    @SerializedName("satis") @Expose var satis : Float,
    @SerializedName("tarih") @Expose var tarih : Float,
    @SerializedName("aciklama") @Expose var aciklama : String,
    @SerializedName("alis_dir") @Expose var alis_dir : Int,
    @SerializedName("satis_dir") @Expose var satis_dir : Int,
    @SerializedName("dusuk") @Expose var dusuk : Float,
    @SerializedName("yuksek") @Expose var yuksek : Float,
    @SerializedName("kapanis") @Expose var kapanis : Float,
) : Serializable {

}
