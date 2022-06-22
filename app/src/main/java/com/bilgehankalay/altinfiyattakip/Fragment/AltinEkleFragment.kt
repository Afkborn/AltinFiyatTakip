package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import com.bilgehankalay.altinfiyattakip.Model.Altin
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.AltinlarResponse
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentAltinEkleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AltinEkleFragment : Fragment() {
    private lateinit var binding : FragmentAltinEkleBinding


    var degerliList : ArrayList<Degerli> = arrayListOf()
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
        degerliGetir()
        setRadioButtonListener()
        showFiyatConstraint()

    }

    private fun setRadioButtonListener(){
        binding.radioGroupSecim.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ _, checkdId ->
            if (checkdId == R.id.radioButton_simdiAldim){
                showFiyatConstraint()
            }
            else if (checkdId == R.id.radioButton_gecmisZamanliAldim){
                showTarihConstraint()
            }
        })
    }

    private fun showFiyatConstraint(){
        binding.constraintLayoutFiyat.visibility = View.VISIBLE
        binding.constraintLayoutTarih.visibility = View.INVISIBLE
    }
    private fun showTarihConstraint(){
        binding.constraintLayoutFiyat.visibility = View.INVISIBLE
        binding.constraintLayoutTarih.visibility = View.VISIBLE
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

                    println("Güncelleme başarılı!")
                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )
    }


}