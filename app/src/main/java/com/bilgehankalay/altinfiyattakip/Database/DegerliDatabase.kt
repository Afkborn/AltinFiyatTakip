package com.bilgehankalay.altinfiyattakip.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bilgehankalay.altinfiyattakip.DAO.DegerliRoomDAO
import com.bilgehankalay.altinfiyattakip.Model.Degerli


@Database(entities = [Degerli::class], version = 8)
abstract class DegerliDatabase : RoomDatabase() {
    abstract fun degerliDAO() : DegerliRoomDAO
    companion object{
        private var INSTANCE : DegerliDatabase? = null

        fun getirDegerliDatabase(context : Context) : DegerliDatabase? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    DegerliDatabase::class.java,
                    "degerli.db"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return INSTANCE
        }
    }
}