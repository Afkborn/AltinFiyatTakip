package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bilgehankalay.altinfiyattakip.Model.Altin
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import com.bilgehankalay.altinfiyattakip.Response.PostAlisSatisResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentHomeScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeScreen : Fragment() {
    private lateinit var binding : FragmentHomeScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TEST
        binding.button.setOnClickListener {
            ApiUtils.altinDAOInterfaceGetir().dateAl("AYAR22","2012-09-10","2012-09-11").enqueue(
                object : Callback<PostAlisSatisResponse>{
                    override fun onResponse(
                        call: Call<PostAlisSatisResponse>,
                        response: Response<PostAlisSatisResponse>
                    ) {
                        val tempList = response.body()?.altinlar
                        val en_buyuk = response.body()?.en_yuksek
                        val en_kucuk = response.body()?.en_dusuk
                        println(en_buyuk)
                        println(en_kucuk)
                        println(tempList)
                    }

                    override fun onFailure(call: Call<PostAlisSatisResponse>, t: Throwable) {
                        println(t.localizedMessage)
                    }

                }
            )
        }
    }



}

