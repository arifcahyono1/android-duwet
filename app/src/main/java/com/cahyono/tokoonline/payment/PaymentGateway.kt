package com.cahyono.tokoonline.payment

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.activity.SuccessActivity
import com.cahyono.tokoonline.adapter.AdapterBank
import com.cahyono.tokoonline.app.ApiConfig
import com.cahyono.tokoonline.model.Bank
import com.cahyono.tokoonline.model.Chekout
import com.cahyono.tokoonline.model.ResponModel
import com.cahyono.tokoonline.model.Transaksi
import com.google.gson.Gson
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.external.UiKitApi
import kotlinx.android.synthetic.main.activity_pengiriman.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentGateway : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        displayPay()
    }

    fun displayPay() {
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-n81rlwsHsWFiWZW9") // client_key is mandatory
            .setContext(this) // context is mandatory
            .setTransactionFinishedCallback(object : TransactionFinishedCallback {
                override fun onTransactionFinished(p0: com.midtrans.sdk.corekit.models.snap.TransactionResult?) {
                    TODO("Not yet implemented")
                }
            }) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("http://192.168.100.75/midtrans/index.php/") //set merchant url (required)
            .enableLog(true) // enable sdk log (optional)
            .setLanguage("id") //`en` for English and `id` for Bahasa
            .buildSDK()



        btn_bayar_pengiriman.setOnClickListener {
            val json = intent.getStringExtra("extra")!!.toString()
            val chekout = Gson().fromJson(json, Chekout::class.java)

            val transactionRequest = TransactionRequest("umkm-duwet-"+System.currentTimeMillis().toShort()+"", chekout.total_harga.toDouble())
            val detail = ItemDetails("NamaItemId", chekout.total_harga.toDouble(), chekout.total_harga.toInt(), "Testing")
            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detail)
            uiKitDetails(transactionRequest)

            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this@PaymentGateway)

            //val transaction = TransactionRequest("UMKM-Duwet-"+System.currentTimeMillis().toShort() + "",)

//            val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//            loading.setTitleText("Loading...").show()
//
//            ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel> {
//                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                    loading.dismiss()
//                    error(t.message.toString())
////                Toast.makeText(this, "Error:" + t.message, Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
//                    loading.dismiss()
//                    if (!response.isSuccessful) {
//                        error(response.message())
//                        return
//                    }
//
//                    val respon = response.body()!!
//                    if (respon.success == 0) {
//
////                        val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
////                        val jsChekout = Gson().toJson(chekout, Chekout::class.java)
//
////                        val intent = Intent(this@PaymentGateway, SuccessActivity::class.java)
////                        intent.putExtra("transaksi", jsTransaksi)
////                        intent.putExtra("chekout", jsChekout)
////                        startActivity(intent)
//
//
//
//
//                    } else {
//                        error(respon.message)
//                        Toast.makeText(this@PaymentGateway, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
        }
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

    fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun bayar(bank: Bank) {
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json, Chekout::class.java)
        chekout.bank = bank.nama

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
//                Toast.makeText(this, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1) {

                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsChekout = Gson().toJson(chekout, Chekout::class.java)

                    val intent = Intent(this@PaymentGateway, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("chekout", jsChekout)
                    startActivity(intent)

                } else {
                    error(respon.message)
                    Toast.makeText(this@PaymentGateway, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}