package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentOnayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class OnayFragment : Fragment() {
    private lateinit var binding : FragmentOnayBinding
    private lateinit var gelenDegerli : Degerli
    private var guncelMi = true
    private val args : OnayFragmentArgs  by navArgs()
    private var sayac = 10
    private var miktar = 0F

    private lateinit var degerliDB : DegerliDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gelenDegerli = args.degerliObject
        guncelMi = args.guncelMi
        miktar = args.miktar
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
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

        binding.onayFabOk.setOnClickListener {
            gelenDegerli.miktar = miktar
            gelenDegerli.isUserData = true
            gelenDegerli.id = 0
            degerliDB.degerliDAO().degerliEkle(gelenDegerli)
            val gecisAction = OnayFragmentDirections.onayToHome()
            findNavController().navigate(gecisAction)
        }
        binding.onayFabClose.setOnClickListener {
            val gecisAction = OnayFragmentDirections.onayToAltinEkle()
            findNavController().navigate(gecisAction)
        }
    }
    private fun getDateTime(s: Float): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date((s * 1000).toLong())
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun degerliYukle(){
        try {
            binding.onayTextViewSatis.text = getString(R.string.onay_screen_satisTextView,gelenDegerli.satis,gelenDegerli.getSembol())
            binding.onayTextViewAlis.text = getString(R.string.onay_screen_alisTextView,gelenDegerli.alis,gelenDegerli.getSembol())
            binding.onayTextViewOzet.text = getString(R.string.onay_screen_ozetTextView,miktar,gelenDegerli.getAciklama(), (miktar * gelenDegerli.satis),gelenDegerli.getAciklama(1))
            binding.onayTextViewTarih.text = getString(R.string.onay_screen_tarihTextView,getDateTime(gelenDegerli.tarih))
        }
        catch (e : Exception){
            println(e.localizedMessage)
        }

    }

    private fun sayacBaslat(){
        if (sayac ==0){
            fiyatGuncelle()
            sayac = 10
        }
        else{
            sayac -= 1
        }
        try {
            binding.onayTextViewSayac.text = getString(R.string.onay_screen_kalan_saniye,sayac)//"${sayac} saniye"
        }
        catch (e : Exception){
            println(e.localizedMessage)
        }


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