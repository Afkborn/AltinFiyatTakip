package com.bilgehankalay.altinfiyattakip.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.DegerliCardTasarimBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DegerliRecyclerAdapter(private var degerliList : ArrayList<Degerli>) : RecyclerView.Adapter<DegerliRecyclerAdapter.DegerliCardTasarim>() {
    class DegerliCardTasarim(val degerliCardTasarimBinding : DegerliCardTasarimBinding) : RecyclerView.ViewHolder(degerliCardTasarimBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DegerliCardTasarim {
        val layoutInflater = LayoutInflater.from(parent.context)
        val degerliCardTasarimBinding = DegerliCardTasarimBinding.inflate(layoutInflater,parent,false)
        return DegerliCardTasarim(degerliCardTasarimBinding)
    }

    override fun onBindViewHolder(holder: DegerliCardTasarim, position: Int) {
        val degerli = degerliList[position]
        holder.degerliCardTasarimBinding.also {
            val sembol = getSembol(degerli.aciklama)
            if (degerli.alis_dir == -1){
                it.imageViewAlisFiyati.setImageResource(R.drawable.red_down_arrow)
            }
            else if (degerli.alis_dir == 1){
                it.imageViewAlisFiyati.setImageResource(R.drawable.green_up_arrow)
            }
            if (degerli.satis_dir == -1){
                it.imageViewSatisFiyati.setImageResource(R.drawable.red_down_arrow)
            }
            else if (degerli.satis_dir == 1){
                it.imageViewSatisFiyati.setImageResource(R.drawable.green_up_arrow)
            }
            it.textViewAciklama.text = degerli.aciklama
            it.textViewAlisFiyati.text = "${degerli.alis} ${sembol}"
            it.textViewSatisFiyati.text = "${degerli.satis} ${sembol}"
            it.textViewCode.text = degerli.code
        }
    }

    override fun getItemCount(): Int {
        return degerliList.size
    }
    private fun getSembol(aciklama:String) : String{
        val sembolList = aciklama.split("/")
        when (sembolList[1]){
            "TL"  -> return "₺"
            "Dolar" -> return "$"
            "Euro" -> return "€"
            "Bulgar Levası" -> return "лв"
            "İsrail Şekeli" -> return "ILS"
            "Fas Dirhemi" -> return "MAD"
            "Katar Riyali" -> return "QAR"
            "Suudi Arabistan Riyali" -> return "SAR"
            "İsveç Kronu" -> return "kr"
            "Japon Yeni" -> return "¥"
            "Norveç Kronu" -> return "NOK"
            "Ruble" -> return "₽"
            "İsviçre Frangı" -> return "CHF"
            "Kanada Doları" -> return " CAD$"
            "Danimarka Kronu" -> return "DKK"
        }
        return ""
    }



}