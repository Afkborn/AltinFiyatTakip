package com.bilgehankalay.altinfiyattakip.Fragment

import android.graphics.Color
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeScreen : Fragment() {
    private lateinit var binding : FragmentHomeScreenBinding

    private lateinit var degerliDB : DegerliDatabase
    private lateinit var myDegerliList : List<Degerli?>

    private var guncelDegerliListArray : ArrayList<Degerli> = arrayListOf()

    private lateinit var degerliRecyclerAdapter : HomeScreenDegerliRecyclerAdapter

    private var toplamMiktar = 0f
    private var toplamKarZarar = 0f

    private var dolarDegerli: Degerli? = null
    private var euroDegerli : Degerli? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
        myDegerliList = degerliDB.degerliDAO().getAllUserDegerli()
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
        degerliRecyclerAdapter = HomeScreenDegerliRecyclerAdapter(myDegerliList)
        binding.homeScreenRecyclerViewDegerli.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.homeScreenRecyclerViewDegerli.adapter = degerliRecyclerAdapter
        binding.homeScreenRecyclerViewDegerli.setHasFixedSize(true)

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                loadDegerliFromDB()
                mainHandler.postDelayed(this,1000)
            }
        })

    }

    private fun updateUI(){
        toplamMiktar = 0F
        toplamKarZarar = 0F
        if (myDegerliList.isNotEmpty()){
            myDegerliList.forEach {  myDegerli ->
                if (myDegerli != null){
                        if (myDegerli.getAciklama(1) == "TL"){
                            toplamMiktar += myDegerli.toplamGuncelDeger
                            toplamKarZarar += myDegerli.karZarar
                        }
                        else if (myDegerli.getAciklama(1) == "Dolar"){
                            toplamMiktar += myDegerli.toplamGuncelDeger * dolarDegerli!!.alis
                            toplamKarZarar +=  myDegerli.karZarar * dolarDegerli!!.alis
                        }
                        else if (myDegerli.getAciklama(1) == "Euro"){
                            toplamMiktar += myDegerli.toplamGuncelDeger * euroDegerli!!.alis
                            toplamKarZarar += myDegerli.karZarar * euroDegerli!!.alis
                        }

                }

            }
        }
        val yuvarlananToplamMiktar = String.format("%.2f",toplamMiktar)
        val yuvarlananToplamKarZarar = String.format("%.2f",toplamKarZarar)
        if (toplamKarZarar >= 0.0f){
            binding.homeScreenTextViewNetKarZarar.setTextColor(Color.parseColor("#42FF00"))
        }
        else {
            binding.homeScreenTextViewNetKarZarar.setTextColor(Color.parseColor("#FF0000"))
        }
        binding.homeScreenTextViewToplam.text = "${yuvarlananToplamMiktar} TL"
        binding.homeScreenTextViewNetKarZarar.text = "${yuvarlananToplamKarZarar} TL"

    }
    private fun loadDegerliFromDB(){
        val degerliListA : List<Degerli?> =  degerliDB.degerliDAO().getAllAPIDegerli()
        if (degerliListA.isNotEmpty()){
            guncelDegerliListArray.clear()
            degerliListA.forEach {
                if (it != null) {
                    guncelDegerliListArray.add(it)
                }
            }
            loadDolarEuroAlisSatis()
            myDegerliList = degerliDB.degerliDAO().getAllUserDegerli()
            myDegerliList.forEach {  myDegerli ->
                if (myDegerli != null){
                    myDegerli.setGuncelDegerli(guncelDegerliListArray.filter { it.code == myDegerli.code }[0])
                }
            }
            updateUI()
            degerliRecyclerAdapter.updateRecyclerAdapter(myDegerliList)
        }
    }

    private fun loadDolarEuroAlisSatis(){
        dolarDegerli = guncelDegerliListArray.filter { it.code == "USDTRY" }[0]
        euroDegerli = guncelDegerliListArray.filter { it.code == "EURTRY" }[0]

    }

}

