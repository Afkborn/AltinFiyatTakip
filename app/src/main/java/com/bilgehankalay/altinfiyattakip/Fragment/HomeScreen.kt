package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bilgehankalay.altinfiyattakip.Adapter.HomeScreenDegerliRecyclerAdapter
import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentHomeScreenBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeScreen : Fragment() {
    private lateinit var binding : FragmentHomeScreenBinding

    private lateinit var degerliDB : DegerliDatabase
    private lateinit var myDegerliList : List<Degerli?>
    private var guncelDegerliList : ArrayList<Degerli> = arrayListOf()

    private lateinit var degerliRecyclerAdapter : HomeScreenDegerliRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
        myDegerliList = degerliDB.degerliDAO().tumKitap()
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
        degerliRecyclerAdapter = HomeScreenDegerliRecyclerAdapter(myDegerliList,guncelDegerliList)
        binding.homeScreenRecyclerViewDegerli.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.homeScreenRecyclerViewDegerli.adapter = degerliRecyclerAdapter
        binding.homeScreenRecyclerViewDegerli.setHasFixedSize(true)

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
            object : Callback<DegerliResponse> {
                override fun onResponse(
                    call: Call<DegerliResponse>,
                    response: Response<DegerliResponse>
                ) {
                    val tempList = response.body()?.altinlar
                    tempList?.let {
                        guncelDegerliList = it as ArrayList<Degerli>
                    }
                    myDegerliList = degerliDB.degerliDAO().tumKitap()
                    degerliRecyclerAdapter.updateRecyclerAdapter(myDegerliList,guncelDegerliList)

                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )
    }

}

