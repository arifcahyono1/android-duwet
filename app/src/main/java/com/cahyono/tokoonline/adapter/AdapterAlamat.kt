package com.cahyono.tokoonline.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.model.Alamat
import com.google.gson.Gson
import com.cahyono.tokoonline.MainActivity
import com.cahyono.tokoonline.activity.DetailProdukActivity
import com.cahyono.tokoonline.activity.LoginActivity
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.room.MyConnection
import com.cahyono.tokoonline.room.MyDatabase
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterAlamat(var activity: Activity, var data: ArrayList<Alamat>, var listener: Listeners) : RecyclerView.Adapter<AdapterAlamat.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_alamat_nama)
        val tvPhone = view.findViewById<TextView>(R.id.tv_alamat_phone)
        val tvAlamat = view.findViewById<TextView>(R.id.tv_alamat_name)
        val layout = view.findViewById<CardView>(R.id.layoutAlamat)
        val rd = view.findViewById<RadioButton>(R.id.rd_alamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_alamat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val a = data[position]

        holder.rd.isChecked = a.isSelected
        holder.tvNama.text = a.name
        holder.tvPhone.text = a.phone
        holder.tvAlamat.text = a.alamat + ", " + a.kota + ", " + a.kecamatan + ", " + a.kodepos + ", (" + a.type + ")"

//        holder.rd.setOnClickListener {
//            a.isSelected = true
//            listener.onClicked(a)
//        }
//
//        holder.layout.setOnClickListener {
//            a.isSelected = true
//            listener.onClicked(a)
//        }
    }

    private fun update(data: Alamat) {
        val myDb = MyConnection.getInstance(activity)
        val alamatActive = myDb?.daoAlamat()?.getByStatus(true)!!
        alamatActive.isSelected = false
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoAlamat().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

//    interface Listeners {
//        fun onClicked(data: Alamat)
//    }

    interface Listeners {
        fun onUpdate()
//        fun onDelete(position: Int)
    }

}