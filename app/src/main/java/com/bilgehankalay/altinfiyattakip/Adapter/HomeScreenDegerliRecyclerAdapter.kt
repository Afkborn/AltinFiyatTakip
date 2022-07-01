package com.bilgehankalay.altinfiyattakip.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.HomeScreenDegerliCardTasarimBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeScreenDegerliRecyclerAdapter(private var myDegerliList : List<Degerli?>) : RecyclerView.Adapter<HomeScreenDegerliRecyclerAdapter.HomeScreenDegerliCardTasarim>() {
    class HomeScreenDegerliCardTasarim(val homeScreenDegerliCardTasarim : HomeScreenDegerliCardTasarimBinding) : RecyclerView.ViewHolder(homeScreenDegerliCardTasarim.root)
    lateinit var context : Context
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

                it.textViewDegerliIsim.text = "${degerli.miktar} ${degerli.getAciklama()}"
                it.textViewDegerliTarih.text = getDateTime(degerli.tarih)
                    //it.textViewDegerliCodeAlis.text = "${degerli.code} (${guncelDegerli!!.alis} ${guncelDegerli!!.getSembol()})"
                    val yuvarlananToplamGuncelDeger = String.format("%.2f",degerli.toplamGuncelDeger)
                    it.textViewDegerliToplam.text = "${yuvarlananToplamGuncelDeger} ${degerli!!.getSembol()}"

                    //kar zarar hesapla
                    val karZarar = degerli.karZarar
                    val yuvarlananKarZarar = String.format("%.2f",karZarar )
                    if (karZarar > 0){

                        it.textViewDegerliKarZarar.setTextColor(ContextCompat.getColor(context,R.color.green))
                    }
                    else{

                        it.textViewDegerliKarZarar.setTextColor(ContextCompat.getColor(context,R.color.redLight))
                    }
                    it.textViewDegerliKarZarar.text = "${yuvarlananKarZarar} ${degerli!!.getSembol()}"

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

    private fun getDateTime(s: Float): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date((s * 1000).toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}