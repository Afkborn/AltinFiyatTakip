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
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentDegerliListeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DegerliListe : Fragment() {
    private var degerliListArray : ArrayList<Degerli> = arrayListOf()
    private lateinit var binding : FragmentDegerliListeBinding
    private lateinit var degerliAdapter : DegerliRecyclerAdapter

    private lateinit var degerliDB : DegerliDatabase
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
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                updateDegerliListFromDB()
                degerliAdapter.setDegerliList(degerliListArray)
                mainHandler.postDelayed(this,1000)
            }
        })
    }
    private fun updateDegerliListFromDB(){
        val degerliListA : List<Degerli?> =  degerliDB.degerliDAO().getAllAPIDegerli()
        if (degerliListA.isNotEmpty()){
            degerliListArray.clear()
            degerliListA.forEach {
                if (it != null) {
                    degerliListArray.add(it)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("degerliListe",degerliListArray)

    }


}