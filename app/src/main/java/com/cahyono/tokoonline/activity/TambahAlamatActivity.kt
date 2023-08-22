package com.cahyono.tokoonline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.app.ApiConfigAlamat
import com.cahyono.tokoonline.helper.Helper
import com.cahyono.tokoonline.model.Alamat
import com.cahyono.tokoonline.model.ModelAlamat
import com.cahyono.tokoonline.model.ResponModel
import com.cahyono.tokoonline.room.MyConnection
import com.cahyono.tokoonline.room.MyDatabase
import com.cahyono.tokoonline.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    lateinit var btn_simpan: Button
    lateinit var edt_nama: EditText
    lateinit var edt_alamat: EditText
    lateinit var edt_phone: EditText
    lateinit var edt_type: EditText
    lateinit var edt_kodePos: EditText
    lateinit var pb: ProgressBar

    lateinit var div_provinsi: RelativeLayout
    lateinit var spn_provinsi: Spinner
    lateinit var div_kota: RelativeLayout
    lateinit var spn_kota: Spinner

    lateinit var myDb : MyConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)

        myDb = MyConnection.getInstance(this)!!

        init()
        mainButton()
        getInfo()
        getProvinsi()
//        Helper().setToolbar(this, toolbar, "Tambah Alamat")
    }

    fun init (){
        btn_simpan = findViewById(R.id.btn_simpan)
        edt_nama = findViewById(R.id.edt_nama)
        edt_alamat = findViewById(R.id.edt_alamat)
        edt_phone = findViewById(R.id.edt_phone)
        edt_type = findViewById(R.id.edt_type)
        edt_kodePos = findViewById(R.id.edt_kodePos)

        pb = findViewById(R.id.pb)
        div_provinsi = findViewById(R.id.div_provinsi)
        spn_provinsi = findViewById(R.id.spn_provinsi)
        div_kota = findViewById(R.id.div_kota)
        spn_kota = findViewById(R.id.spn_kota)
    }

    private fun mainButton() {
        btn_simpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan() {
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0") {
            toast("Silahkan pilih provinsi")
            return
        }

        if (kota.city_id == "0") {
            toast("Silahkan pilih Kota")
            return
        }

        val alamat = Alamat()
        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province
        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name
        alamat.id_kecamatan = kecamatan.id
        alamat.kecamatan = kecamatan.nama
        alamat.isSelected = true

        insert(alamat)
    }

    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText) {
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    fun getInfo(){
        var tlbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        // Set toolbarsindar@mail.com
        setSupportActionBar(tlbar)
        supportActionBar!!.title = "Alamat Pengiriman"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun getProvinsi() {
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")

                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi) {
                        arryString.add(prov.province)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                provinsi = listProvinsi[position - 1]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }
                    }

                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun getKota(id: String) {
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")
                    for (kota in listArray) {
                        arryString.add(kota.type + " " + kota.city_name)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                kota = listArray[position - 1]
                                val kodePos = kota.postal_code
                                edt_kodePos.setText(kodePos)
//                                getKecamatan(idKota)
                            }
                        }
                    }
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun insert(data: Alamat) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert data success")
                onBackPressed()
            })


        Log.d("Response","Berhasil di input")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}