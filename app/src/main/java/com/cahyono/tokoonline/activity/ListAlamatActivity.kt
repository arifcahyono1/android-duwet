package com.cahyono.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.adapter.AdapterAlamat
import com.cahyono.tokoonline.model.Alamat
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.room.MyConnection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListAlamatActivity : AppCompatActivity() {
    lateinit var myDb : MyConnection
    lateinit var btnTambah: Button
    lateinit var div_kosong: LinearLayout
    lateinit var rv_alamat: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)
        myDb = MyConnection.getInstance(this)!!
        init()
        main()
    }

    private fun displayAlamat() {
        val arrayList = myDb.daoAlamat().getAll() as ArrayList

        if (arrayList.isEmpty()) div_kosong.visibility = View.VISIBLE
        else div_kosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        rv_alamat.adapter = AdapterAlamat(arrayList, object : AdapterAlamat.Listeners {
//            override fun onClicked(data: Alamat) {
//                if (myDb.daoAlamat().getByStatus(true) != null){
//                    val alamatActive = myDb.daoAlamat().getByStatus(true)!!
//                    alamatActive.isSelected = false
//                    updateActive(alamatActive, data)
//                }
//            }
//        })
        rv_alamat.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Alamat, dataNonActive: Alamat) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
                val listProduk = myDb.daoAlamat().getAll() // get All data
                for(produk : Alamat in listProduk){
                    println("-----------------------")
                    println(produk.name)
                    println(produk.isSelected)
                }
            })
    }

    private fun updateNonActive(data: Alamat) {
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
//                onBackPressed()
//                OnBackPressedDispatcher.add
//                OnBackPressedDispatcher.addCallback
//                onBackPressed()
//                onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//
//                    }
//                })
                val listProduk = myDb.daoAlamat().getAll() // get All data
                for(produk : Alamat in listProduk){
                    println("-----------------------")
                    println(produk.name)
                    println(produk.isSelected)
                }
            })
    }

    fun init(){

        val OnBackPressedCallback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        }

        btnTambah = findViewById(R.id.btn_tambahAlamat)
        div_kosong = findViewById(R.id.div_list)
        rv_alamat = findViewById(R.id.rv_alamat)
    }

    fun main(){
        btnTambah.setOnClickListener {
//            startActivity(Intent(this, TambahAlamatActivity::class.java))
            val listProduk = myDb.daoAlamat().getAll() // get All data
            for(produk : Alamat in listProduk){
                println("-----------------------")
                println(produk.name)
                println(produk.isSelected)
            }
        }
    }

    override fun onResume() {
        displayAlamat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}