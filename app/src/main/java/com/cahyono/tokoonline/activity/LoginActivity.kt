package com.cahyono.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.cahyono.tokoonline.MainActivity
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.app.ApiConfig
import com.cahyono.tokoonline.helper.SharedPref
import com.cahyono.tokoonline.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById(R.id.btn_prosesLogin) as Button

        s = SharedPref(this)

        login.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                login()
            }
        })
    }

    fun login(){
        val email = findViewById<EditText>(R.id.edt_email)
        val password = findViewById<EditText>(R.id.edt_password)

        val pb = findViewById<ProgressBar>(R.id.pb)

        if(email.text.isEmpty()){
            email.error = "Email tidak boleh kosong"
            email.requestFocus()
            return
        } else if(password.text.isEmpty()){
            password.error = "password tidak boleh kosong"
            password.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.login(email.text.toString(),password.text.toString()).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!

                pb.visibility = View.GONE

                if (respon.success == 1){
                    s.setStatusLogin(true)
                    s.setUser(respon.user)

                    val intent  = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@LoginActivity, respon.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Error: "+respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}