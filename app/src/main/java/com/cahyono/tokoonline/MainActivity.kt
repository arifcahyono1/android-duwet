package com.cahyono.tokoonline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.cahyono.tokoonline.activity.LoginActivity
import com.cahyono.tokoonline.activity.MasukActivity
import com.cahyono.tokoonline.databinding.ActivityMainBinding
import com.cahyono.tokoonline.fragment.AboutFragment
import com.cahyono.tokoonline.fragment.AkunFragment
import com.cahyono.tokoonline.fragment.HomeFragment
import com.cahyono.tokoonline.fragment.KeranjangFragment
import com.cahyono.tokoonline.helper.SharedPref

class MainActivity : AppCompatActivity() {
    
    val fargmentHome: Fragment = HomeFragment()
    val fargmentKeranjang: Fragment = KeranjangFragment()
    val fargmentAkun: Fragment = AkunFragment()
    val fm: FragmentManager = supportFragmentManager
    val active: Fragment = fargmentHome

    private var statusLogin = false

    private lateinit var s:SharedPref

    lateinit var rvProduk: RecyclerView

    private lateinit var binding : ActivityMainBinding

    private var dariDetail: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> {replaceFragment(HomeFragment())}
                R.id.keranjang -> replaceFragment(KeranjangFragment())
                R.id.about -> replaceFragment(AboutFragment())
                R.id.akun ->
                    if (s.getStatusLogin()){
                        replaceFragment(AkunFragment())
                    } else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }
                else ->{

                }
            }
            true
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
    }

    val mMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container,fragment)
        fragmentTransaction.commit()

    }

    override fun onResume() {
        if (dariDetail) {
            dariDetail = false
            replaceFragment(KeranjangFragment())
        }
        super.onResume()
    }
}