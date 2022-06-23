package com.bilgehankalay.altinfiyattakip.Fragment


import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController

import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R

import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentAltinEkleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*
import kotlin.collections.ArrayList


class AltinEkleFragment : Fragment() {
    private lateinit var binding : FragmentAltinEkleBinding
    private lateinit var seciliDegerli : Degerli
    private var isLoaded = false
    var degerliList : ArrayList<Degerli> = arrayListOf()
    private var guncelMi = true
    private lateinit var dayMonthYearText : String

    private var alinacakMiktar : Float = 0F
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
        isLoaded = false
        setRadioButtonListener()
        showFiyatConstraint()
        setEditTextListener()
        setSpinnerListener()
        binding.buttonTarihSec.setOnClickListener{
            setDatePickerDialog()
        }
        binding.buttonEkle.setOnClickListener {
            if (guncelMi){
                val gecisAction = AltinEkleFragmentDirections.altinEkleToOnay(seciliDegerli,true)
                findNavController().navigate(gecisAction)
            }
            else{
                val gecisAction = AltinEkleFragmentDirections.altinEkleToOnay(seciliDegerli,false)
                findNavController().navigate(gecisAction)
            }

        }
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                degerliGetir()
                mainHandler.postDelayed(this,10000)
            }
        })

    }

    private fun setSpinnerListener() {
        binding.spinnerDegerli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.altinEkleEditTextFrom.setText("")
                binding.altinEkleEditTextTo.setText("")

                seciliDegerli = degerliList[p2]
                binding.altinEkleTextViewSatisFiyati.text = "${seciliDegerli.satis} ${seciliDegerli.getSembol()}"
                binding.altinEkleTextViewAlisFiyati.text = "${seciliDegerli.alis} ${seciliDegerli.getSembol()}"

                if (guncelMi){
                    val splitedAciklama = seciliDegerli.aciklama.split("/")
                    binding.altinEkleTextViewFromText.text = splitedAciklama[0]
                    binding.altinEkleTextViewToText.text = splitedAciklama[1]
                }

                if (seciliDegerli.alis_dir == -1){
                    binding.altinEkleImageViewAlisFiyati.setImageResource(R.drawable.red_down_arrow)
                }
                else if (seciliDegerli.alis_dir == 1){
                    binding.altinEkleImageViewAlisFiyati.setImageResource(R.drawable.green_up_arrow)
                }
                if (seciliDegerli.satis_dir == -1){
                    binding.altinEkleImageViewSatisFiyati.setImageResource(R.drawable.red_down_arrow)
                }
                else if (seciliDegerli.satis_dir == 1){
                    binding.altinEkleImageViewSatisFiyati.setImageResource(R.drawable.green_up_arrow)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun setRadioButtonListener(){
        binding.radioGroupSecim.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ _, checkdId ->
            if (checkdId == R.id.radioButton_simdiAldim){
                showFiyatConstraint()
                guncelMi = true
            }
            else if (checkdId == R.id.radioButton_gecmisZamanliAldim){
                showTarihConstraint()
                guncelMi = false
            }
        })
    }


    private fun showFiyatConstraint(){

        binding.constraintLayoutTarih.visibility = View.INVISIBLE
        binding.altinEkleLinearLayoutFiyat.visibility = View.VISIBLE
        binding.constraintLayoutMiktarSimdi.visibility = View.VISIBLE
    }
    private fun showTarihConstraint(){

        binding.constraintLayoutTarih.visibility = View.VISIBLE
        binding.altinEkleLinearLayoutFiyat.visibility = View.INVISIBLE
        binding.constraintLayoutMiktarSimdi.visibility = View.INVISIBLE
    }

    private fun setDatePickerDialog(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth->
            var monthOfYearReal = (monthOfYear + 1).toString()
            var dayOfMonthReal = dayOfMonth.toString()
            if (monthOfYearReal.toInt() < 10 ){
                monthOfYearReal = "0${monthOfYearReal}"
            }
            if (dayOfMonthReal.toInt() < 10){
                dayOfMonthReal = "0${dayOfMonthReal}"
            }
            dayMonthYearText = "${dayOfMonthReal}.${monthOfYearReal}.${year}"
            binding.editTextDate.setText(dayMonthYearText)
        }, year, month, day)
        dpd.show()
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
                    if (!isLoaded){
                        spinnerYukle()
                        isLoaded = true
                    }

                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )
    }

    private fun spinnerYukle(){
        val degerliIsimlerListe : ArrayList<String> = arrayListOf()
        degerliList.forEach {
            degerliIsimlerListe.add(it.aciklama)
        }
        val adapter : ArrayAdapter<*>
        adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            degerliIsimlerListe
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDegerli.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setEditTextListener(){

        binding.altinEkleEditTextFrom.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.length >= 0){
                    val p0F = p0.toString().toFloatOrNull()
                    if (p0F != null){
                        alinacakMiktar = p0F
                        binding.altinEkleEditTextTo.setText("${alinacakMiktar * seciliDegerli.satis}")
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })




    }


}