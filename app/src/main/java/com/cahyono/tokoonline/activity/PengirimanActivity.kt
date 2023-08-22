
package com.cahyono.tokoonline.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cahyono.tokoonline.BuildConfig.BASE_URL
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.adapter.AdapterKurir
import com.cahyono.tokoonline.app.ApiConfig
import com.cahyono.tokoonline.app.ApiConfigAlamat
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.helper.SharedPref
import com.cahyono.tokoonline.model.Bank
import com.cahyono.tokoonline.model.Chekout
import com.cahyono.tokoonline.model.ResponModel
import com.cahyono.tokoonline.model.Transaksi
import com.cahyono.tokoonline.model.rajaongkir.Costs
import com.cahyono.tokoonline.model.rajaongkir.ResponOngkir
import com.cahyono.tokoonline.room.MyConnection
import com.cahyono.tokoonline.room.MyDatabase
import com.cahyono.tokoonline.util.ApiKey
import com.google.android.gms.common.internal.service.Common.CLIENT_KEY
import com.google.gson.Gson
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.SdkUIFlowBuilder.*
import com.midtrans.sdk.uikit.api.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PengirimanActivity : AppCompatActivity() {

    lateinit var myDb: MyConnection
    lateinit var myDbTwo: MyDatabase
    lateinit var btnTambahAlamat: Button
    lateinit var spn_kurir: Spinner
    lateinit var spn_hari: Spinner
    lateinit var rv_metode: RecyclerView
    lateinit var tv_ongkir: TextView
    lateinit var tv_total: TextView
    lateinit var div_alamat: CardView
    lateinit var div_kosong: TextView
    lateinit var div_metodePengiriman: LinearLayout
    lateinit var tv_nama: TextView
    lateinit var tv_phone: TextView
    lateinit var tv_alamat: TextView
    lateinit var tv_totalBelanja: TextView
    lateinit var tv_total_belanja: TextView
    lateinit var btn_bayar: Button

    var totalHarga = 0

    val hari = arrayOf("Rabu", "Sabtu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
//        Helper().setToolbar(this, toolbar, "Pengiriman")
        myDb = MyConnection.getInstance(this)!!
        myDbTwo = MyDatabase.getInstance(this)!!
        init()


        val listProduk = myDbTwo.daoName().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
//        val produks = ArrayList<Chekout.Item>()
        for (p in listProduk) {
            if (p.selected) {
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                tv_totalBelanja.text = Helper().gantiRupiah(totalHarga)
            }
        }
        main()
        setSepiner()
        getInfo()
    }

    fun setSepiner() {
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }
        }

        val adapterHari = ArrayAdapter<Any>(this, R.layout.item_spinner, hari)
        adapterHari.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_hari.adapter = adapterHari
        spn_hari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    spn_hari.selectedItem.toString()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun chekAlamat() {



        if (myDb.daoAlamat().getByStatus(true) != null) {
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            val alamatLengkap = a.alamat + ", " + a.kota + ", " + a.kodepos + ", (" + a.type + ")"
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = alamatLengkap
            btnTambahAlamat.text = "Ubah Alamat"

            getOngkir("JNE")

//            tv_total_belanja = (totalHarga + Integer.valueOf(ongkir)
        } else {
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btnTambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun getOngkir(kurir: String) {

        val alamat = myDb.daoAlamat().getByStatus(true)

        val origin = "255"
        val destination = "" + alamat!!.id_kota.toString()
        val berat = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object :
            Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {
                if (response.isSuccessful) {

                    Log.d("Success", "berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()) {
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }

                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("Error", "gagal memuat data:" + t.message)
            }

        })
    }

    var ongkir = "0"
    var kurir = ""
    var jasaKirim = ""

    private fun displayOngkir(_kurir: String, arrayList: ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices) {
            val ongkir = arrayList[i]
            if (i == 0) {
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        kurir = _kurir
        jasaKirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners {
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir) {
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = _kurir
                jasaKirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    fun setTotal(ongkir: String) {
        val listProduk = myDbTwo.daoName().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
//        val produks = ArrayList<Chekout.Item>()
        for (p in listProduk) {
            if (p.selected) {
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                tv_totalBelanja.text = Helper().gantiRupiah(totalHarga)
                tv_ongkir.text = Helper().gantiRupiah(ongkir)
                tv_total.text = Helper().gantiRupiah(Integer.valueOf(ongkir) + totalHarga)
            }
        }
    }

    fun init(){
        btnTambahAlamat = findViewById(R.id.btn_tambahAlamatPengiriman)
        btn_bayar = findViewById(R.id.btn_bayar_pengiriman)
        spn_kurir = findViewById(R.id.spn_kurir)
        spn_hari = findViewById(R.id.spn_hari)
        rv_metode = findViewById(R.id.rv_metode)
        tv_ongkir = findViewById(R.id.tv_ongkir)
        tv_total = findViewById(R.id.tv_total_belanja)

        div_alamat = findViewById(R.id.div_alamat)
        div_kosong = findViewById(R.id.div_kosong)
        div_metodePengiriman = findViewById(R.id.div_metodePengiriman)

        tv_nama = findViewById(R.id.tv_nama_alamat)
        tv_phone = findViewById(R.id.tv_phone_alamat)
        tv_alamat = findViewById(R.id.tv_alamat_alamat)
        tv_totalBelanja = findViewById(R.id.tv_totalBelanja)
        tv_total_belanja = findViewById(R.id.tv_total_belanja)

//        btn_tambahAlamat = findViewById(R.id.btn_tambahAlamatPengiriman)
    }

    fun bayar(){
        //to be

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-n81rlwsHsWFiWZW9") // client_key is mandatory
            .setContext(applicationContext) // context is mandatory
            .setTransactionFinishedCallback({
                    result -> isDestroyed
            }) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("https://api.readytowork.site/midtrans.php/") //set merchant url (required)
            .enableLog(true) // enable sdk log (optional)
            .setLanguage("id")
            .setColorTheme(com.midtrans.sdk.corekit.core.themes.CustomColorTheme("#002855", "#002855", "#002855"))
            .buildSDK()

        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamat().getByStatus(true)!!

        val listProduk = myDbTwo.daoName().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Chekout.Item>()
        for (p in listProduk) {
            if (p.selected) {
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                val produk = Chekout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga))
                produk.catatan = "catatan baru"
                produks.add(produk)
            }
        }

        val chekout = Chekout()
        chekout.user_id = "" + user.id
        chekout.total_item = "" + totalItem
        chekout.total_harga = "" + totalHarga
        chekout.name = a.name
        chekout.phone = a.phone
        chekout.jasa_pengiriaman = jasaKirim
        chekout.ongkir = ongkir
        chekout.kurir = kurir
        chekout.detail_lokasi = tv_alamat.text.toString()
        chekout.total_transfer = "" + (totalHarga + Integer.valueOf(ongkir))
        chekout.produks = produks
        chekout.bank = "Midtrans"
        chekout.hari = spn_hari.selectedItem.toString()

        val totalTf = totalHarga + Integer.valueOf(ongkir)

        val json = Gson().toJson(chekout, Chekout::class.java)
        Log.d("Respon:", "json:" + json)

        val transactionRequest = TransactionRequest("umkm-duwet-"+System.currentTimeMillis().toShort()+"", totalTf.toDouble())
        println(totalTf)
        val itemDetails = ArrayList<ItemDetails>()
        for(pm in listProduk){

            val detail = com.midtrans.sdk.corekit.models.ItemDetails("", pm.harga.toDouble(), pm.jumlah, "Testing")
            itemDetails.add(detail)
            for (i in itemDetails){
                println("-----")
                println(i.id)

                println(i.price)
            }
        }

        val ongkir = com.midtrans.sdk.corekit.models.ItemDetails("", chekout.ongkir.toDouble(), 1, "Ongkir")
        itemDetails.add(ongkir)

        uiKitDetails(transactionRequest)
        transactionRequest.itemDetails = itemDetails
        MidtransSDK.getInstance().transactionRequest = transactionRequest
        MidtransSDK.getInstance().startPaymentUiFlow(this)

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                loading.dismiss()
                error(t.message.toString())
//                Toast.makeText(this, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
//                loading.dismiss()
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1) {

//                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsChekout = Gson().toJson(chekout, Chekout::class.java)

//                    val intent = Intent(this@PengirimanActivity, SuccessActivity::class.java)
//                    intent.putExtra("transaksi", jsTransaksi)
//                    intent.putExtra("chekout", jsChekout)
//                    startActivity(intent)

                } else {
                    error(respon.message)
                    Toast.makeText(this@PengirimanActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        val ts =  TimeZone.getDefault()
//        Log.d("time", System.currentTimeMillis().toShort())
        itemDetails.clear()
    }

    fun uiKitDetails(transactionRequest: TransactionRequest){
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = "Arif"
        customerDetails.phone = "01928031203"
        customerDetails.email = "rod@mail.com"
        customerDetails.firstName = "Arif"
        customerDetails.lastName = "Cahyono"

        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Arjosari"
        shippingAddress.city = "Malang"
        shippingAddress.postalCode = "65162"
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = "Arjosari"
        billingAddress.city = "Malang"
        billingAddress.postalCode = "65162"
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
    }

    fun getInfo(){
        var tlbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        // Set toolbarsindar@mail.com
        setSupportActionBar(tlbar)
        supportActionBar!!.title = "Pengiriman"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun main(){
        btnTambahAlamat.setOnClickListener {
            startActivity(Intent(this, TambahAlamatActivity::class.java))
        }

        btn_bayar.setOnClickListener{
            bayar()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        chekAlamat()
        super.onResume()
    }
}