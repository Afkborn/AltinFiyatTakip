package com.bilgehankalay.altinfiyattakip.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.altinfiyattakip.Model.Degerli
import com.bilgehankalay.altinfiyattakip.databinding.HomeScreenDegerliCardTasarimBinding

class HomeScreenDegerliRecyclerAdapter(private var myDegerliList : List<Degerli?>, private var guncelDegerliList : ArrayList<Degerli>) : RecyclerView.Adapter<HomeScreenDegerliRecyclerAdapter.HomeScreenDegerliCardTasarim>() {
    class HomeScreenDegerliCardTasarim(val homeScreenDegerliCardTasarim : HomeScreenDegerliCardTasarimBinding) : RecyclerView.ViewHolder(homeScreenDegerliCardTasarim.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeScreenDegerliCardTasarim {
        val layoutInflater = LayoutInflater.from(parent.context)
        val homeScreenDegerliCardTasarimBinding = HomeScreenDegerliCardTasarimBinding.inflate(layoutInflater,parent,false)
        return HomeScreenDegerliCardTasarim(homeScreenDegerliCardTasarimBinding)
    }

    override fun onBindViewHolder(holder: HomeScreenDegerliCardTasarim, position: Int) {
        val degerli = myDegerliList[position]
        var guncelDegerli : Degerli? = null

        if (degerli != null){
            guncelDegerliList.forEach {
                if (it.code == degerli.code){
                    guncelDegerli =it
                }
            }
            holder.homeScreenDegerliCardTasarim.also {
                it.textViewDegerliIsim.text = "${degerli.getAciklama()}"

                if (guncelDegerli != null){
                    val toplamEskiDeger = degerli.miktar * degerli.satis
                    it.textViewDegerliCodeAlis.text = "${degerli.code} (${guncelDegerli!!.alis} ${guncelDegerli!!.getSembol()})"

                    val toplamGuncelDeger = degerli.miktar * guncelDegerli!!.alis
                    val yuvarlananToplamGuncelDeger = String.format("%.2f",toplamGuncelDeger)
                    it.textViewDegerliToplam.text = "${yuvarlananToplamGuncelDeger} ${guncelDegerli!!.getSembol()}"

                    //kar zarar hesapla
                    val karZarar =  toplamGuncelDeger - toplamEskiDeger
                    val yuvarlananKarZarar = String.format("%.2f", karZarar)
                    if (karZarar > 0){
                        it.textViewDegerliKarZarar.setTextColor(Color.parseColor("#42FF00")) //renk yeşil
                    }
                    else{
                        it.textViewDegerliKarZarar.setTextColor(Color.parseColor("#FF0000")) // renk kırmızı
                    }
                    it.textViewDegerliKarZarar.text = "${yuvarlananKarZarar} ${guncelDegerli!!.getSembol()}"

                }
                else{
                    it.textViewDegerliCodeAlis.text = "${degerli.code}"
                }

            }

        }

    }

    override fun getItemCount(): Int {
        return myDegerliList.size
    }

    fun updateRecyclerAdapter(newMyDegerliList : List<Degerli?>, newGuncelDegerliList : ArrayList<Degerli>){
        this.myDegerliList = newMyDegerliList
        this.guncelDegerliList = newGuncelDegerliList
        this.notifyDataSetChanged()
    }

}