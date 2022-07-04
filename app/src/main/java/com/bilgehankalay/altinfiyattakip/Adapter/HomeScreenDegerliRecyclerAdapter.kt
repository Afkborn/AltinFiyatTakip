package com.bilgehankalay.altinfiyattakip.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Global.DATE_FORMAT_PATTERN
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.HomeScreenDegerliCardTasarimBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeScreenDegerliRecyclerAdapter(private var myDegerliList : List<Degerli?>) : RecyclerView.Adapter<HomeScreenDegerliRecyclerAdapter.HomeScreenDegerliCardTasarim>() {
    class HomeScreenDegerliCardTasarim(val homeScreenDegerliCardTasarim : HomeScreenDegerliCardTasarimBinding) : RecyclerView.ViewHolder(homeScreenDegerliCardTasarim.root)
    lateinit var context : Context
    var onItemClick : (Degerli) -> Unit = {}
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeScreenDegerliCardTasarim {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val homeScreenDegerliCardTasarimBinding = HomeScreenDegerliCardTasarimBinding.inflate(layoutInflater,parent,false)
        return HomeScreenDegerliCardTasarim(homeScreenDegerliCardTasarimBinding)
    }

    override fun onBindViewHolder(holder: HomeScreenDegerliCardTasarim, position: Int) {
        val degerli = myDegerliList[position]
        if (degerli != null){
            holder.homeScreenDegerliCardTasarim.also {
                it.textViewDegerliTarih.text = context.getString(R.string.home_screen_degerli_ra_tarih,getDateTime(degerli.tarih)) //getDateTime(degerli.tarih)
                it.textViewDegerliToplam.text = context.getString(R.string.home_screen_degerli_ra_toplam, degerli.toplamGuncelDeger, degerli.getSembol())
                it.textViewDegerliKarZarar.text = context.getString(R.string.home_screen_degerli_ra_karZarar, degerli.karZarar, degerli.getSembol())

                if (degerli.isAltin || degerli.miktar.toInt().toFloat() == degerli.miktar){
                    it.textViewDegerliIsim.text = context.getString(R.string.home_screen_degerli_ra_isimAltin,degerli.miktar.toInt(),degerli.getAciklama())
                }
                else{
                    it.textViewDegerliIsim.text = context.getString(R.string.home_screen_degerli_ra_isimDoviz,degerli.miktar,degerli.getAciklama())
                }
                if (degerli.karZarar > 0){
                    it.textViewDegerliKarZarar.setTextColor(ContextCompat.getColor(context,R.color.green))
                }
                else{
                    it.textViewDegerliKarZarar.setTextColor(ContextCompat.getColor(context,R.color.redLight))
                }
                it.root.setOnClickListener{
                    onItemClick(degerli)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return myDegerliList.size
    }

    fun updateRecyclerAdapter(newMyDegerliList : List<Degerli?>){
        this.myDegerliList = newMyDegerliList
        this.notifyDataSetChanged()
    }

    private fun getDateTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat(DATE_FORMAT_PATTERN)
            val netDate = Date((s * 1000))
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}