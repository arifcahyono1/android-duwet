package com.cahyono.tokoonline.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cahyono.tokoonline.MainActivity
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.activity.LoginActivity
import com.cahyono.tokoonline.activity.RiwayatActivity
import com.cahyono.tokoonline.helper.SharedPref

/**
 * A simple [Fragment] subclass.
 */
class AkunFragment : Fragment() {

    lateinit var s:SharedPref
    lateinit var btnLogout:TextView
    lateinit var tvNama:TextView
    lateinit var tvPhone:TextView
    lateinit var tvEmail:TextView
    lateinit var btnListAlamat:RelativeLayout
    lateinit var btnKeranjang:RelativeLayout
    lateinit var btnTentang:RelativeLayout
    lateinit var btnRiwayat: RelativeLayout

    lateinit var sm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)

            btnLogout = view.findViewById<TextView>(R.id.btn_logout)
        tvNama = view.findViewById<TextView>(R.id.tv_nama)
        tvEmail = view.findViewById<TextView>(R.id.tv_email)
        tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        btnListAlamat = view.findViewById(R.id.btn_settingAlamat)
        btnKeranjang = view.findViewById(R.id.btn_keranjang)
        btnTentang = view.findViewById(R.id.btn_tentang)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)

        s = SharedPref(requireActivity())

        btnLogout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                s.setStatusLogin(false)

                activity?.let{
                    val intent = Intent (it, MainActivity::class.java)
                    it.startActivity(intent)
                }
            }
        })

        btnRiwayat.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }

        btnListAlamat.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("geo:-8.0101374,112.8098788"))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            val chooser = Intent.createChooser(intent, "Lauch Maps")

            startActivity(chooser)
        }

        btnKeranjang.setOnClickListener {
//            startActivity(Intent(requireActivity(), KeranjangFragment::class.java))
            val keranjang = KeranjangFragment()
            sm.beginTransaction().replace(R.id.keranjang, keranjang).commit()
        }

        btnTentang.setOnClickListener {
            startActivity(Intent(requireActivity(), AboutFragment::class.java))
        }

        setData()
        return view
    }

//    private fun replaceFragment(fragment : Fragment){
//
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.container,fragment)
//        fragmentTransaction.commit()
//
//    }

    fun setData(){
        if (s.getUser() == null){
            val intent  = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            return
        }

        val user = s.getUser()!!

        tvNama.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
    }

//    private fun init(view: View) {
//        btnLogout = view.findViewById<TextView>(R.id.btn_logout)
//        tvNama = view.findViewById<TextView>(R.id.tv_nama)
//        tvEmail = view.findViewById<TextView>(R.id.tv_email)
//        tvPhone = view.findViewById<TextView>(R.id.tv_phone)
//
//    }
}