package com.bilgehankalay.altinfiyattakip.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.DegerliCardTasarimBinding
import kotlin.collections.ArrayList

class DegerliRecyclerAdapter(private var degerliList : ArrayList<Degerli>) : RecyclerView.Adapter<DegerliRecyclerAdapter.DegerliCardTasarim>() {
    class DegerliCardTasarim(val degerliCardTasarimBinding : DegerliCardTasarimBinding) : RecyclerView.ViewHolder(degerliCardTasarimBinding.root)
    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DegerliCardTasarim {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val degerliCardTasarimBinding = DegerliCardTasarimBinding.inflate(layoutInflater,parent,false)
        return DegerliCardTasarim(degerliCardTasarimBinding)
    }

    override fun onBindViewHolder(holder: DegerliCardTasarim, position: Int) {
        val degerli = degerliList[position]
        holder.degerliCardTasarimBinding.also {
            val sembol = degerli.getSembol()
            if (degerli.alis_dir == -1){
                it.degerliCardTasarimImageViewAlisFiyati.setImageResource(R.drawable.red_down_arrow)
                it.degerliCardTasarimTextViewAlisFiyati.setTextColor(ContextCompat.getColor(context,R.color.redLight))
            }
            else if (degerli.alis_dir == 1){
                it.degerliCardTasarimImageViewAlisFiyati.setImageResource(R.drawable.green_up_arrow)
                it.degerliCardTasarimTextViewAlisFiyati.setTextColor(ContextCompat.getColor(context,R.color.green))
            }
            if (degerli.satis_dir == -1){
                it.degerliCardTasarimImageViewSatisFiyati.setImageResource(R.drawable.red_down_arrow)
                it.degerliCardTasarimTextViewSatisFiyati.setTextColor(ContextCompat.getColor(context,R.color.redLight))
            }
            else if (degerli.satis_dir == 1){
                it.degerliCardTasarimImageViewSatisFiyati.setImageResource(R.drawable.green_up_arrow)
                it.degerliCardTasarimTextViewSatisFiyati.setTextColor(ContextCompat.getColor(context,R.color.green))
            }
            it.degerliCardTasarimTextViewAciklama.text = degerli.aciklama
            it.degerliCardTasarimTextViewAlisFiyati.text = "${degerli.alis} ${sembol}"
            it.degerliCardTasarimTextViewSatisFiyati.text = "${degerli.satis} ${sembol}"
            it.degerliCardTasarimTextViewCode.text = degerli.code
        }
    }

    override fun getItemCount(): Int {
        return degerliList.size

    }


    fun setDegerliList(gelen_degerliList : ArrayList<Degerli>) {
        degerliList = gelen_degerliList
        this.notifyDataSetChanged()
    }




}