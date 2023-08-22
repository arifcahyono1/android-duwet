package com.cahyono.tokoonline.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.activity.DetailProdukActivity
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterProduk(var activity: Activity, var data: ArrayList<Produk>):RecyclerView.Adapter<AdapterProduk.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
        val imgProduk: ImageView = itemView.findViewById(R.id.img_produk)

        val layout: CardView = itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = data[position]

        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().gantiRupiah(data[position].harga)

        val image = Config.productUrl + data[position].image

        Picasso.get()
            .load(image)
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.imgProduk)

        holder.layout.setOnClickListener{
            val intent = Intent(activity, DetailProdukActivity::class.java)

            val str = Gson().toJson(data[position], Produk::class.java)

            intent.putExtra("extra", str)

            activity.startActivity(intent)
        }
    }
}