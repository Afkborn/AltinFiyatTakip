package com.bilgehankalay.altinfiyattakip.Network

import com.bilgehankalay.altinfiyattakip.DAO.AltinDAOInterface

class ApiUtils {

    companion object{
        private const val BASE_URL = "https://raspberypi.afkborn.keenetic.pro"

        fun altinDAOInterfaceGetir() : AltinDAOInterface{
            return RetrofitClient.getClient(BASE_URL).create(AltinDAOInterface::class.java)
        }

    }
}