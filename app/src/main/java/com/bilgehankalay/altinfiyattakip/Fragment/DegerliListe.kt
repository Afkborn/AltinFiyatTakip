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
import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase
import com.bilgehankalay.altinfiyattakip.Global.DB_REFRESH_TIME
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentDegerliListeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DegerliListe : Fragment() {

    private lateinit var binding : FragmentDegerliListeBinding
    private lateinit var degerliAdapter : DegerliRecyclerAdapter
    private lateinit var degerliDB : DegerliDatabase
    private var degerliListArray : ArrayList<Degerli> = arrayListOf()
    private var altinMi = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
        if (savedInstanceState != null){
            degerliListArray = savedInstanceState.getSerializable("degerliListe") as ArrayList<Degerli>
        }
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
        degerliAdapter = DegerliRecyclerAdapter(degerliListArray)
        binding.recyclerViewDegerli.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewDegerli.adapter = degerliAdapter
        binding.recyclerViewDegerli.setHasFixedSize(true)

        binding.degerliListeSwitchAltinDoviz.setOnClickListener {
            if (binding.degerliListeSwitchAltinDoviz.isChecked){
                altinMi = false
                binding.degerliListeSwitchAltinDoviz.setText(R.string.altinEkle_screen_switch_on)
                binding.degerliListeSwitchAltinDoviz.setText(R.string.altinEkle_screen_dovizler)
                updateDegerliListFromDB()
            }
            else{
                altinMi = true
                binding.degerliListeSwitchAltinDoviz.setText(R.string.altinEkle_screen_switch_off)
                binding.degerliListeSwitchAltinDoviz.setText(R.string.altinEkle_screen_altinlar)
                updateDegerliListFromDB()

            }
        }

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                updateDegerliListFromDB()
                mainHandler.postDelayed(this, DB_REFRESH_TIME)
            }
        })
    }
    private fun updateDegerliListFromDB(){
        val degerliListA : List<Degerli?> = if (altinMi){
            degerliDB.degerliDAO().getAllAPIAltın()
        } else{
            degerliDB.degerliDAO().getAllAPIDoviz()
        }
        if (degerliListA.isNotEmpty()){
            degerliListArray.clear()
            degerliListA.forEach {
                if (it != null) {
                    degerliListArray.add(it)
                }
            }
            degerliAdapter.setDegerliList(degerliListArray)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("degerliListe",degerliListArray)

    }


}