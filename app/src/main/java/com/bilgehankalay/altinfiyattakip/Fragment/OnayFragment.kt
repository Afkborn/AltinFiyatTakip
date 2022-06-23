package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentOnayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OnayFragment : Fragment() {
    private lateinit var binding : FragmentOnayBinding
    private lateinit var gelenDegerli : Degerli
    private var guncelMi = true
    private val args : OnayFragmentArgs  by navArgs()
    private var sayac = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gelenDegerli = args.degerliObject
        guncelMi = args.guncelMi

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnayBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        degerliYukle()
        if (guncelMi){
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post(object:Runnable{
                override fun run() {
                    sayacBaslat()
                    mainHandler.postDelayed(this,1000)
                }
            })
        }
        else{

        }
    }
    private fun degerliYukle(){
        binding.onayTextViewSatis.text = "${gelenDegerli.satis} ${gelenDegerli.getSembol()}"
        binding.onayTextViewAlis.text = "${gelenDegerli.alis} ${gelenDegerli.getSembol()}"

    }

    private fun sayacBaslat(){
        if (sayac ==0){
            fiyatGuncelle()
            sayac = 10
        }
        else{
            sayac -= 1
        }
        binding.onayTextViewSayac.text = "${sayac} saniye"

    }
    private fun fiyatGuncelle(){
        ApiUtils.altinDAOInterfaceGetir().altinAlV2(gelenDegerli.code).enqueue(
            object: Callback<DegerliResponse>{
                override fun onResponse(
                    call: Call<DegerliResponse>,
                    response: Response<DegerliResponse>
                ) {
                    gelenDegerli = response.body()?.altinlar!![0]
                    degerliYukle()
                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )

    }


}