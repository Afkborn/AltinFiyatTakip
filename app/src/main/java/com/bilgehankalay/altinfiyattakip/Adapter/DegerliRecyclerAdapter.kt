package com.bilgehankalay.altinfiyattakip.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.DegerliCardTasarimBinding
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
            val sembol = degerli.getSembol()
            if (degerli.alis_dir == -1){
                it.degerliCardTasarimImageViewAlisFiyati.setImageResource(R.drawable.red_down_arrow)
                it.degerliCardTasarimTextViewAlisFiyati.setTextColor(Color.parseColor("#FF0000"))
            }
            else if (degerli.alis_dir == 1){
                it.degerliCardTasarimImageViewAlisFiyati.setImageResource(R.drawable.green_up_arrow)
                it.degerliCardTasarimTextViewAlisFiyati.setTextColor(Color.parseColor("#42FF00"))
            }
            if (degerli.satis_dir == -1){
                it.degerliCardTasarimImageViewSatisFiyati.setImageResource(R.drawable.red_down_arrow)
                it.degerliCardTasarimTextViewSatisFiyati.setTextColor(Color.parseColor("#FF0000"))
            }
            else if (degerli.satis_dir == 1){
                it.degerliCardTasarimImageViewSatisFiyati.setImageResource(R.drawable.green_up_arrow)
                it.degerliCardTasarimTextViewSatisFiyati.setTextColor(Color.parseColor("#42FF00"))
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


    fun setDegerliList(gelen_degerliList : ArrayList<Degerli>){
        degerliList = gelen_degerliList
        this.notifyDataSetChanged()
    }




}