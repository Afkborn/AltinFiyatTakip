package com.bilgehankalay.altinfiyattakip.Response

import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostAlisSatisResponse(
    @SerializedName("status") @Expose var status : Int,
    @SerializedName("code") @Expose var code : String,
    @SerializedName("tarih_baslangic") @Expose var tarih_baslangic : String,
    @SerializedName("tarih_bitis") @Expose var tarih_bitis : String,
    @SerializedName("en_dusuk") @Expose var en_dusuk : Float,
    @SerializedName("en_yuksek") @Expose var en_yuksek : Float,
    @SerializedName("alis_satis_gunler") @Expose var altinlar : List<Degerli>
)
