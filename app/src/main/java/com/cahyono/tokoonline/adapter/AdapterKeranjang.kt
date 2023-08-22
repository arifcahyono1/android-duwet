package com.cahyono.tokoonline.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.activity.DetailProdukActivity
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.room.MyDatabase
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterKeranjang(var activity: Activity, var data: ArrayList<Produk>, var listener: Listeners):RecyclerView.Adapter<AdapterKeranjang.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_jumlah)
        val imgProduk: ImageView = itemView.findViewById(R.id.img_keranjang)

        val btnTambah: ImageView = itemView.findViewById(R.id.btn_tambah)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
        val btnKurang: ImageView = itemView.findViewById(R.id.btn_kurang)

        val checkbox: CheckBox = itemView.findViewById(R.id.checkBox)

        val layout: CardView = itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoName().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    private fun delete(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoName().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = data[position]

        var jumlah = data[position].jumlah

        holder.tvNama.text = produk.name
        holder.tvJumlah.text = jumlah.toString()
        holder.tvHarga.text = Helper().gantiRupiah(data[position].harga)

        holder.checkbox.isChecked = produk.selected
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            produk.selected = isChecked
            update(produk)
        }

        val image = "https://dev.kobis.id/storage/produk/"+data[position].image

        Picasso.get()
            .load(image)
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.imgProduk)

        holder.btnTambah.setOnClickListener {
            if (jumlah == 10){
                Toast.makeText(activity, "Produk mencapai batas maksimal", Toast.LENGTH_SHORT).show()
            } else {
                jumlah++
                holder.tvJumlah.text = jumlah.toString()

                produk.jumlah = jumlah
                update(produk)
            }
        }

        holder.btnKurang.setOnClickListener {
            if (jumlah == 1){
                Toast.makeText(activity, "Produk mencapai batas minimum", Toast.LENGTH_SHORT).show()
            } else {
                jumlah--
                holder.tvJumlah.text = jumlah.toString()

                produk.jumlah = jumlah
                update(produk)
            }
        }

        holder.btnDelete.setOnClickListener {
            delete(produk)
            listener.onDelete(position)
        }
    }
}