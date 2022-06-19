package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bilgehankalay.altinfiyattakip.Model.Altin
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentAltinEkleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AltinEkleFragment : Fragment() {
    private lateinit var binding : FragmentAltinEkleBinding
    var altinlarList : ArrayList<Altin> = arrayListOf()
    val altinIsimListe : ArrayList<String> = arrayListOf()
    private lateinit var seciliAltin : Altin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAltinEkleBinding.inflate(inflater,container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerListener()
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
                    spinnerAltinAdlariYukle()
                }

                override fun onFailure(call: Call<AltinlarResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    private fun spinnerAltinAdlariYukle(){
        altinIsimListe.clear()
        altinlarList.forEach {
            altinIsimListe.add(it.altinAdi)
        }
        val adapter : ArrayAdapter<*>
        adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            altinIsimListe
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAltinlar.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setSpinnerListener(){
        binding.spinnerAltinlar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                seciliAltin = altinlarList.get(p2)
                println(seciliAltin)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }


}