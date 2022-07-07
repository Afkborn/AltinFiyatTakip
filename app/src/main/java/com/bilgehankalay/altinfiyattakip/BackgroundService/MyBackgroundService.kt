package com.bilgehankalay.altinfiyattakip.BackgroundService

import android.app.Service
import android.content.Intent
import android.os.IBinder

import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase

import com.bilgehankalay.altinfiyattakip.Global.BG_REFRESH_TIME
import com.bilgehankalay.altinfiyattakip.Model.Degerli

import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyBackgroundService : Service() {
    private var guncelDegerliList : ArrayList<Degerli> = arrayListOf()
    private lateinit var degerliDB : DegerliDatabase

    private fun degerliGetir(){
        ApiUtils.altinDAOInterfaceGetir().altinlariAlV2().enqueue(
            object : Callback<DegerliResponse> {
                override fun onResponse(
                    call: Call<DegerliResponse>,
                    response: Response<DegerliResponse>
                ) {
                    val tempList = response.body()?.altinlar
                    tempList?.let {
                        guncelDegerliList = it as ArrayList<Degerli>
                    }
                    guncelDegerliList.forEach {
                        it.isUserData = false
                        it.isAltin = "Altın" in it.aciklama
                        val result = degerliDB.degerliDAO().getIDFromAPIDegerli(it.code)
                        if (result == null)
                            degerliDB.degerliDAO().degerliEkle(it)
                        else{
                            it.id = result
                            degerliDB.degerliDAO().degerliGuncelle(it)
                        }
                    }
                }
                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    if (t.localizedMessage == "timeout"){
                        println("Sunucu offline")
                    }
                    else{
                        println(t.localizedMessage)
                    }

                }
            }
        )
    }







    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        degerliDB = DegerliDatabase.getirDegerliDatabase(this)!!

        Thread {
            while (true) {
                degerliGetir()
                Thread.sleep(BG_REFRESH_TIME)

            }
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}