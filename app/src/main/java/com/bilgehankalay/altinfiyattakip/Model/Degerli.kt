package com.bilgehankalay.altinfiyattakip.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "degerliler")
data class Degerli(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id : Int = 0,
    @ColumnInfo(name = "code")  @SerializedName("code") @Expose var code : String,
    @ColumnInfo(name = "alis")  @SerializedName("alis") @Expose var alis : Float,
    @ColumnInfo(name = "satis") @SerializedName("satis") @Expose var satis : Float,
    @ColumnInfo(name = "tarih")  @SerializedName("tarih") @Expose var tarih : Long,
    @ColumnInfo(name = "miktar") var miktar : Float,
    @ColumnInfo(name = "isUserData") var isUserData : Boolean,
    @ColumnInfo(name = "isAltin") var isAltin : Boolean,
    @SerializedName("aciklama") @Expose var aciklama : String,
    @SerializedName("alis_dir") @Expose var alis_dir : Int,
    @SerializedName("satis_dir") @Expose var satis_dir : Int,
    @SerializedName("dusuk") @Expose var dusuk : Float,
    @SerializedName("yuksek") @Expose var yuksek : Float,
    @SerializedName("kapanis") @Expose var kapanis : Float,

    ) : Serializable {

    @Ignore var toplamEskiDeger: Float = 0.0f
    @Ignore var toplamGuncelDeger : Float = 0.0f
    @Ignore var karZarar : Float = 0.0f

    fun setGuncelDegerli(guncelDegerli: Degerli){
        toplamEskiDeger = miktar * satis
        toplamGuncelDeger = guncelDegerli.alis * miktar
        karZarar = toplamGuncelDeger - toplamEskiDeger
    }

    fun getToplamGuncelDeger():String{
        if (toplamGuncelDeger.toInt().toFloat() == toplamGuncelDeger){
             return toplamGuncelDeger.toInt().toString()
        }
        return String.format("%.2f", toplamGuncelDeger)
    }

    fun getKarZarar() : String{
        if (karZarar.toInt().toFloat() == karZarar){
            return karZarar.toInt().toString()
        }
        return String.format("%.2f", karZarar)
    }


    fun getSembol(type : Int = 1) : String{
        val sembolList = aciklama.split("/")
        when (sembolList[type]){
            "TL"  -> return "₺"
            "Dolar" -> return "$"
            "Euro" -> return "€"
            "Bulgar Levası" -> return "лв"
            "İsrail Şekeli" -> return "ILS"
            "Fas Dirhemi" -> return "MAD"
            "Katar Riyali" -> return "QAR"
            "Suudi Arabistan Riyali" -> return "SAR"
            "İsveç Kronu" -> return "kr"
            "Japon Yeni" -> return "¥"
            "Norveç Kronu" -> return "NOK"
            "Ruble" -> return "₽"
            "İsviçre Frangı" -> return "CHF"
            "Kanada Doları" -> return " CAD$"
            "Danimarka Kronu" -> return "DKK"
        }
        return ""
    }

    fun getAciklama(type : Int = 0) : String{
        val aciklamaList = aciklama.split("/")
        if (type == 0){
            if ("Yeni" in aciklamaList[type]  || "Eski" in aciklamaList[type]){
                return aciklamaList[type].replace("Yeni","").replace("Eski","")
            }
        }
        return aciklamaList[type]
    }
}
