package com.bilgehankalay.altinfiyattakip.DAO

import androidx.room.*
import com.bilgehankalay.altinfiyattakip.Model.Degerli

@Dao
interface DegerliRoomDAO {
    @Insert
    fun degerliEkle(degerli : Degerli)

    @Update
    fun degerliGuncelle(degerli : Degerli)

    @Delete
    fun degerliSil(degerli:Degerli)

    @Query("SELECT * FROM degerliler")
    fun tumKitap() : List<Degerli?>

}