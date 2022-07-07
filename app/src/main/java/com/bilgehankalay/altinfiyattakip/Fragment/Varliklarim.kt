package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase
import com.bilgehankalay.altinfiyattakip.Global.DB_REFRESH_TIME
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.PostAlisSatisResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentVarliklarimBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Varliklarim : Fragment() {
    private lateinit var binding : FragmentVarliklarimBinding
    private lateinit var degerliDB : DegerliDatabase
    private val args : VarliklarimArgs by navArgs()
    private lateinit var selectedDegerli : Degerli
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
        selectedDegerli = args.clickedDegerli
        println(selectedDegerli)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVarliklarimBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
                runBlocking {
                    launch {
                        getAllFiyatByCode(selectedDegerli.code)
                    }
                }

        }
    }


    private fun getAllFiyatByCode(code: String){
        val startTime = System.currentTimeMillis()
        var allFiyatList : ArrayList<Degerli> = arrayListOf()
        ApiUtils.altinDAOInterfaceGetir().allDateAl(code).enqueue(
            object  : Callback<PostAlisSatisResponse> {
                override fun onResponse(
                    call: Call<PostAlisSatisResponse>,
                    response: Response<PostAlisSatisResponse>
                ) {
                    val tempList  = response.body()?.altinlar
                    tempList?.let {
                        allFiyatList = it as ArrayList<Degerli>
                    }
                    val finishTime = System.currentTimeMillis()
                    println("Getting ${code} finished in  ${(finishTime - startTime) / 1000.0} seconds.")


                }

                override fun onFailure(call: Call<PostAlisSatisResponse>, t: Throwable) {
                    println("ERROR: ${t.localizedMessage}")
                }

            }
        )
    }

}