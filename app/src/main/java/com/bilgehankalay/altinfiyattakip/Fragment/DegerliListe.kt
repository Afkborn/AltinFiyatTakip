package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bilgehankalay.altinfiyattakip.Adapter.DegerliRecyclerAdapter
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentDegerliListeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DegerliListe : Fragment() {
    var degerliList : ArrayList<Degerli> = arrayListOf()
    private lateinit var binding : FragmentDegerliListeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDegerliListeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                degerliGetir()
                mainHandler.postDelayed(this,10000)
            }

        })


    }

    private fun degerliGetir(){
        ApiUtils.altinDAOInterfaceGetir().altinlariAlV2().enqueue(
            object : Callback<DegerliResponse>{
                override fun onResponse(
                    call: Call<DegerliResponse>,
                    response: Response<DegerliResponse>
                ) {
                    val tempList = response.body()?.altinlar
                    tempList?.let {
                        degerliList = it as ArrayList<Degerli>
                    }
                    setDegerliAdapter()
                    println("Güncelleme başarılı!")
                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )
    }
    private fun setDegerliAdapter(){
        val degerliAdapter = DegerliRecyclerAdapter(degerliList)
        binding.recyclerViewDegerli.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewDegerli.adapter = degerliAdapter
        binding.recyclerViewDegerli.setHasFixedSize(true)
    }

}