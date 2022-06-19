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
import com.bilgehankalay.altinfiyattakip.databinding.FragmentHomeScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeScreen : Fragment() {
    private lateinit var binding : FragmentHomeScreenBinding

    var altinlarList : ArrayList<Altin> = arrayListOf()

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
        altinlariGetir()
    }

    private fun altinlariGetir(){
        ApiUtils.altinDAOInterfaceGetir().altinlariAl().enqueue(
            object : Callback<AltinlarResponse> {
                override fun onResponse(
                    call: Call<AltinlarResponse>,
                    response: Response<AltinlarResponse>
                ) {
                    val tempList = response.body()?.altinlar
                    tempList?.let {
                        altinlarList = it as ArrayList<Altin>
                    }
                }

                override fun onFailure(call: Call<AltinlarResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

}

