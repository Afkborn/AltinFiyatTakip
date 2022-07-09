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
import com.bilgehankalay.altinfiyattakip.Global.DATE_FORMAT_PATTERN
import com.bilgehankalay.altinfiyattakip.Global.DB_REFRESH_TIME
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.PostAlisSatisResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentVarliklarimBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Varliklarim : Fragment() {
    private lateinit var binding : FragmentVarliklarimBinding
    private lateinit var degerliDB : DegerliDatabase
    private val args : VarliklarimArgs by navArgs()
    private lateinit var selectedDegerli : Degerli
    var allFiyatList : ArrayList<Degerli> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        degerliDB = DegerliDatabase.getirDegerliDatabase(requireContext())!!
        selectedDegerli = args.clickedDegerli
        loadUI()
        getAllFiyatByCode(selectedDegerli.code)

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


    }

    private fun arearangeMixedLine(data : Array<Any>): AAChartModel {
        return AAChartModel()
            .title("TITLE")
            .subtitle("SUB TITLE")
            .series(arrayOf(
                AASeriesElement()
                    .name("PRICE")
                    .color("#1E90FF")
                    .type(AAChartType.Line)
                    .data(data)
                    .zIndex(0)
                )
            )
    }

    private fun getAllFiyatByCode(code: String){
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
                    loadChart()
                }
                override fun onFailure(call: Call<PostAlisSatisResponse>, t: Throwable) {
                    println("ERROR: ${t.localizedMessage}")
                }
            }
        )
    }

    private fun loadChart(){
        val data  : MutableList<Any> = arrayListOf()
        allFiyatList.forEach {
            data.add(arrayOf(it.tarih,it.satis))
        }
        binding.aaChartView.aa_drawChartWithChartModel(arearangeMixedLine(data.toTypedArray()))
    }
    private fun loadUI(){

    }

}