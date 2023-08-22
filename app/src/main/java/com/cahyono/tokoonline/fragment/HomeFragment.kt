package com.cahyono.tokoonline.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import androidx.viewpager.widget.ViewPager
import com.cahyono.tokoonline.MainActivity
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.adapter.AdapterProduk
import com.cahyono.tokoonline.adapter.AdapterSlider
import com.cahyono.tokoonline.app.ApiConfig
import com.cahyono.tokoonline.helper.SharedPref
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvTerlaris: RecyclerView

    lateinit var s: SharedPref

    private lateinit var produkAdapter: AdapterProduk
    private lateinit var produkList: ArrayList<Produk>
    private lateinit var terlarisAdapter: AdapterProduk
    private lateinit var terlarisList: ArrayList<Produk>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)

        var arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.sampul)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        getProduk()

        return view
    }

    fun displayProduk(){
        rvProduk.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rvProduk.setHasFixedSize(true)

        rvTerlaris.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rvTerlaris.setHasFixedSize(true)

        produkList = ArrayList()
        terlarisList = ArrayList()

//        arrTerlarsis()

        produkAdapter = AdapterProduk(requireActivity(), listProduk)
        rvProduk.adapter = produkAdapter

        terlarisAdapter = AdapterProduk(requireActivity(), listProduk)
        rvTerlaris.adapter = terlarisAdapter

    }

    private var listProduk: ArrayList<Produk> = ArrayList()

    fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object :
            Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!

                if(res.success == 1) {
                    listProduk = res.produks
                    displayProduk()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

        })
    }

    fun init(view: View){
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvTerlaris = view.findViewById(R.id.rv_terlaris)
    }
}