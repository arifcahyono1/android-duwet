package com.cahyono.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.helper.SharedPref

class MasukActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        s = SharedPref(this)

        mainButton()
    }

    private fun mainButton(){
        val buttonLogin = findViewById<Button>(R.id.btn_login)
        val buttonRegis = findViewById<Button>(R.id.btn_register)
        buttonLogin.setOnClickListener {
            //cdoe that executes when user taps on the button
            startActivity(Intent(this, LoginActivity::class.java))
        }

        buttonRegis.setOnClickListener {
            //cdoe that executes when user taps on the button
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}