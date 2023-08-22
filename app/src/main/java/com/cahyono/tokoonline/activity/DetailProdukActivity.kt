package com.cahyono.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.data.MyConnection
import com.cahyono.tokoonline.data.Name
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.room.MyDatabase
import com.cahyono.tokoonline.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailProdukActivity : AppCompatActivity() {

    lateinit var myDb : MyDatabase
    lateinit var produk: Produk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!!

        val btn_keranjang = findViewById<RelativeLayout>(R.id.btn_keranjang)
        val btn_toKeranjang = findViewById<RelativeLayout>(R.id.btn_toKeranjang)
        val btn_beliSekarang = findViewById<RelativeLayout>(R.id.btn_beliSekarang)

        getInfo()
        checkKeranjang()

        btn_beliSekarang.setOnClickListener {
            val data = myDb!!.daoName().getProduk(produk.id)
            if (data == null) {
                saveName()
            } else {
                data.jumlah += 1
                update(data)
            }

            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }

        btn_toKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }


        btn_keranjang.setOnClickListener {
        val data = myDb!!.daoName().getProduk(produk.id)
            if (data == null) {
                saveName()
            } else {
                data.jumlah += 1
                update(data)
            }
//            Log.d("Response","Berhasil di input")

        }
    }

    private fun update(data: Produk) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoName().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun saveName() {

        CompositeDisposable().add(Observable.fromCallable { myDb.daoName().insertAll(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this@DetailProdukActivity, "Produk di masukkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkKeranjang(){
        val dataKeranjang = myDb.daoName().getAll()
        var div_angka = findViewById<RelativeLayout>(R.id.div_angka)
        var tvAngka = findViewById<TextView>(R.id.tv_angka)


        if(dataKeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tvAngka.text = dataKeranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }


    fun getInfo(){
        val data = intent.getStringExtra("extra")

        produk = Gson().fromJson<Produk>(data, Produk::class.java)

        //Set Value
        var tvNama = findViewById<TextView>(R.id.tv_nama)
        var tvHarga = findViewById<TextView>(R.id.tv_harga)
        var tvDesc = findViewById<TextView>(R.id.tv_deskripsi)
        var image = findViewById<ImageView>(R.id.image)

        var tlbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        tvNama.text = produk.name
        tvHarga.text = Helper().gantiRupiah(produk.harga)
        tvDesc.text = produk.deskripsi

        val img = "https://dev.kobis.id/storage/produk/" + produk.image

        Picasso.get()
            .load(img)
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(image)

        // Set toolbarsindar@mail.com
        setSupportActionBar(tlbar)
        supportActionBar!!.title = produk.name
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}