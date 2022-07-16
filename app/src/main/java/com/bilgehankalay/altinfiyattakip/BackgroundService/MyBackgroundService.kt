package com.bilgehankalay.altinfiyattakip.BackgroundService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bilgehankalay.altinfiyattakip.Activity.SplashActivity

import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase

import com.bilgehankalay.altinfiyattakip.Global.BG_REFRESH_TIME
import com.bilgehankalay.altinfiyattakip.Global.isServerOnline
import com.bilgehankalay.altinfiyattakip.Model.Degerli

import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.Response.ServerStatusResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HTTP


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
                    println(t.localizedMessage)
                    isServerOnline = false
                }
            }
        )
    }

    private fun getServerStatus(){
        ApiUtils.serverDAOInterfaceGetir().getServerStatus().enqueue(
            object : Callback<ServerStatusResponse>{
                override fun onResponse(
                    call: Call<ServerStatusResponse>,
                    response: Response<ServerStatusResponse>,
                ){
                    try{
                        when (response.body()!!.status){
                            200 -> isServerOnline = true
                        }
                    }
                    catch (e : Exception){

                        isServerOnline = false
                    }

                }
                override fun onFailure(call: Call<ServerStatusResponse>, t: Throwable) {
                    isServerOnline = false
                }
            }
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        degerliDB = DegerliDatabase.getirDegerliDatabase(this)!!
        Thread {
            getServerStatus()
            Thread.sleep(2000)
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