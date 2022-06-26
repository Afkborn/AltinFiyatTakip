package com.bilgehankalay.altinfiyattakip.Fragment


import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bilgehankalay.altinfiyattakip.Database.DegerliDatabase
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.Network.ApiUtils
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.Response.DegerliResponse
import com.bilgehankalay.altinfiyattakip.Response.PostAlisSatisResponse
import com.bilgehankalay.altinfiyattakip.databinding.FragmentAltinEkleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.abs


class AltinEkleFragment : Fragment() {
    private lateinit var binding : FragmentAltinEkleBinding
    private lateinit var seciliDegerli : Degerli
    private var isLoaded = false
    var degerliList : ArrayList<Degerli> = arrayListOf()
    private var guncelMi = true
    private lateinit var dayMonthYearText : String

    private var alinacakMiktar : Float = 0F
    var tarihT1 = ""
    var tarihT2 = ""
    private var ui_epoch = 0F
    private var degerliIsimlerListe : ArrayList<String> = arrayListOf()
    private lateinit var spinnerAdapter : ArrayAdapter<*>
    var isClickeble_Ekle = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            degerliList = savedInstanceState.getSerializable("degerliList") as ArrayList<Degerli> /* = java.util.ArrayList<com.bilgehankalay.altinfiyattakip.Model.Degerli> */
            degerliIsimlerListe.clear()
            degerliList.forEach {
                degerliIsimlerListe.add(it.aciklama)
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAltinEkleBinding.inflate(inflater,container, false)
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

        isLoaded = false
        setRadioButtonListener()
        showFiyatConstraint()
        setEditTextListener()


        spinnerAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            degerliIsimlerListe
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDegerli.adapter = spinnerAdapter
        setSpinnerListener()

        binding.buttonTarihSec.setOnClickListener{
            setDatePickerDialog()
        }
        binding.buttonEkle.setOnClickListener {
            if (guncelMi){
                val gecisAction = AltinEkleFragmentDirections.altinEkleToOnay(seciliDegerli,true, alinacakMiktar)
                findNavController().navigate(gecisAction)
            }
            else{
                val gecmisMiktar = binding.altinEkleEditTextGecmisMiktar.text.toString().toFloatOrNull()
                if (tarihT1 == "" || tarihT2 == ""){
                    Toast.makeText(requireContext(),"Tarih seçin",Toast.LENGTH_LONG).show()
                }
                else if ((gecmisMiktar == null) || (gecmisMiktar <= 0F)){
                    Toast.makeText(requireContext(),"Bir değer girin ",Toast.LENGTH_LONG).show()
                }
                else{
                    if (isClickeble_Ekle){
                        isClickeble_Ekle = !isClickeble_Ekle
                        ApiUtils.altinDAOInterfaceGetir().dateAl(seciliDegerli.code,tarihT1,tarihT2).enqueue(
                            object : Callback<PostAlisSatisResponse>{
                                override fun onResponse(
                                    call: Call<PostAlisSatisResponse>,
                                    response: Response<PostAlisSatisResponse>
                                ) {
                                    val tempDegerli = response.body()?.altinlar
                                    if (tempDegerli != null){
                                        var enYakinTarih = 9999999999
                                        var enYakinIndex = 0
                                        tempDegerli.forEachIndexed { index, degerli ->
                                            val fark = abs(degerli.tarih.toLong() - ui_epoch)
                                            if ( fark < enYakinTarih ){
                                                enYakinIndex = index
                                                enYakinTarih = fark.toLong()
                                            }
                                        }
                                        val gelenDegerli = tempDegerli[enYakinIndex]
                                        println(gelenDegerli)
                                        gelenDegerli.code = seciliDegerli.code
                                        gelenDegerli.aciklama = seciliDegerli.aciklama
                                        val gecisAction = AltinEkleFragmentDirections.altinEkleToOnay(gelenDegerli,false, alinacakMiktar)
                                        findNavController().navigate(gecisAction)
                                    }
                                    else{
                                        ManuallyEnterPriceDialogFragment().show(
                                            childFragmentManager, ManuallyEnterPriceDialogFragment.TAG
                                        )
                                        isClickeble_Ekle  = !isClickeble_Ekle
                                    }
                                }

                                override fun onFailure(
                                    call: Call<PostAlisSatisResponse>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(requireContext(),"Sunucu hatası daha sonra tekrar deneyin",Toast.LENGTH_LONG).show()
                                }

                            }
                        )
                    }
                    else{
                        Toast.makeText(requireContext(),"Yanıt bekleniyor.",Toast.LENGTH_LONG).show()
                    }


                }


            }

        }


    }

    private fun setSpinnerListener() {
        binding.spinnerDegerli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.altinEkleEditTextFrom.setText("")
                binding.altinEkleEditTextTo.setText("")

                seciliDegerli = degerliList[p2]
                binding.altinEkleTextViewSatisFiyati.text = "${seciliDegerli.satis} ${seciliDegerli.getSembol()}"
                binding.altinEkleTextViewAlisFiyati.text = "${seciliDegerli.alis} ${seciliDegerli.getSembol()}"

                val splitedAciklama = seciliDegerli.aciklama.split("/")
                binding.altinEkleTextViewFromText.text = splitedAciklama[0]
                binding.altinEkleTextViewToText.text = splitedAciklama[1]
                binding.altinEkleTextViewFromTextTarih.text = splitedAciklama[0]

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
            val dayMonthYearTextMonthStr = monthOfYear + 1
            dayMonthYearText = "${dayOfMonth}.${dayMonthYearTextMonthStr}.${year}"
            val calendarTarih = Calendar.getInstance()
            calendarTarih.set(year,monthOfYear,dayOfMonth)
            ui_epoch = (calendarTarih.timeInMillis / 1000).toFloat()

            calendarTarih.add(Calendar.MONTH,1)
            calendarTarih.add(Calendar.DAY_OF_MONTH,-4)
            tarihT1 = "${calendarTarih.get(Calendar.YEAR)}-${calendarTarih.get(Calendar.MONTH)}-${calendarTarih.get(Calendar.DAY_OF_MONTH)}"


            calendarTarih.add(Calendar.DAY_OF_MONTH,8) // 8 gün eklemekteki amaç çıkartılan 4 gün önce ve 4 gün sonrayı hesaplamak
            tarihT2 = "${calendarTarih.get(Calendar.YEAR)}-${calendarTarih.get(Calendar.MONTH)}-${calendarTarih.get(Calendar.DAY_OF_MONTH)}"

            println(tarihT1)
            println(tarihT2)

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
                        spinnerAdapter.notifyDataSetChanged()
                        isLoaded = true
                    }
                    degerliIsimlerListe.clear()
                    degerliList.forEach {
                        degerliIsimlerListe.add(it.aciklama)
                    }
                    spinnerAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<DegerliResponse>, t: Throwable) {
                    println(t.localizedMessage)
                }

            }
        )
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

        binding.altinEkleEditTextGecmisMiktar.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.length >= 0){
                    val p0F = p0.toString().toFloatOrNull()
                    if (p0F != null){
                        alinacakMiktar = p0F
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("degerliList", degerliList)
    }
}