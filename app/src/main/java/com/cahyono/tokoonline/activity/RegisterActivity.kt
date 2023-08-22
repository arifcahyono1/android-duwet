package com.cahyono.tokoonline.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.app.ApiConfig
import com.cahyono.tokoonline.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val register = findViewById(R.id.btn_prosesRegister) as Button

        register.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                register()
            }
        })
    }

    fun register(){
        val nama = findViewById<EditText>(R.id.edt_nama)
        val email = findViewById<EditText>(R.id.edt_email)
        val noHp = findViewById<EditText>(R.id.edt_hp)
        val password = findViewById<EditText>(R.id.edt_password)

        val pbRegister = findViewById<ProgressBar>(R.id.pbRegister)

        if(nama.text.isEmpty()){
            nama.error = "Nama tidak boleh kosong"
            nama.requestFocus()
            return
        } else if(email.text.isEmpty()){
            email.error = "Email tidak boleh kosong"
            email.requestFocus()
            return
        } else if(noHp.text.isEmpty()){
            noHp.error = "Nomor HP tidak boleh kosong"
            noHp.requestFocus()
            return
        } else if(password.text.isEmpty()){
            password.error = "Password tidak boleh kosong"
            password.requestFocus()
            return
        }

        pbRegister.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(nama.text.toString(), email.text.toString(),noHp.text.toString(),password.text.toString()).enqueue(object : Callback<ResponModel>{

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                pbRegister.visibility = View.GONE

                if (respon.success == 1){
                    val intent  = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@RegisterActivity, "Pendaftaran Berhasil, Silahkan Login", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error: "+respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: "+t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}