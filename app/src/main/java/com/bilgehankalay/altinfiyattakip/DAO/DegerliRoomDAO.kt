package com.bilgehankalay.altinfiyattakip.DAO

import androidx.room.*
import com.bilgehankalay.altinfiyattakip.Model.Degerli

@Dao
interface DegerliRoomDAO {
    @Insert()
    fun degerliEkle(degerli : Degerli)

    @Update
    fun degerliGuncelle(degerli : Degerli)

    @Delete
    fun degerliSil(degerli:Degerli)

    @Query("SELECT * FROM degerliler WHERE isUserData == 1 ")
    fun getAllUserDegerli() : List<Degerli?>

    @Query("SELECT * FROM degerliler WHERE isUserData == 0 ")
    fun getAllAPIDegerli() : List<Degerli?>

    @Query("SELECT * FROM degerliler WHERE isUserData == 0 AND isAltin == 1")
    fun getAllAPIAltın() : List<Degerli?>

    @Query("SELECT * FROM degerliler WHERE isUserData == 0 AND isAltin == 0")
    fun getAllAPIDoviz() : List<Degerli?>

    @Query("SELECT id FROM degerliler WHERE isUserData == 0 AND code == :code")
    fun getIDFromAPIDegerli(code : String) : Int?


}