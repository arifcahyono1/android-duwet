package com.cahyono.tokoonline.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.cahyono.tokoonline.MainActivity
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Bank
import com.cahyono.tokoonline.model.Chekout
import com.cahyono.tokoonline.model.Transaksi
import com.cahyono.tokoonline.room.MyConnection
import com.cahyono.tokoonline.room.MyDatabase
import kotlinx.android.synthetic.main.activity_success.*
import kotlinx.android.synthetic.main.toolbar.*

class SuccessActivity : AppCompatActivity() {

    var nominal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        Helper().setToolbar(this, toolbar, "Bank Transfer")

        setValues()
        mainButton()
    }

    fun mainButton() {
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
    }

    fun copyText(text: String) {
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Text berhasil di Kopi", Toast.LENGTH_LONG).show()
    }

    fun setValues() {
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("chekout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val chekout = Gson().fromJson(jsCheckout, Chekout::class.java)

        // hapus keranjang
        val myDb = MyDatabase.getInstance(this)!!
        for (produk in chekout.produks){
            myDb.daoName().deleteById(produk.id)
        }

        tv_nomorRekening.text = "Via midtrans"
        tv_namaPenerima.text = "UMKM Duwet"
//        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(chekout.total_transfer)
        tv_nominal.text = Helper().gantiRupiah(nominal)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}
