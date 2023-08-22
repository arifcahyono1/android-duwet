package com.cahyono.tokoonline.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.cahyono.tokoonline.R
import com.cahyono.tokoonline.adapter.AdapterSlider

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var tvContent: TextView
    lateinit var tvContentTwo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_about, container, false)

        init(view)

        var arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.sampul)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        tvContent.setText("Desa Duwet Dusun Kedampul merupakan salah satu dusun yang berada di desa Duwet Kecamatan Tumpang Kabupaten Malang, Jawa Timur. Dusun Kedampul merupakan satu dari dua dusun yang berada di Desa Duwet yang mencakup 4 RW dan 28 RT. Luas wilayah dusun Kedampul sendiri yaitu terbentang dan memanjang dari timur ke barat dengan luas wilayahnya mencakup 1.213 Ha. Dusun Kedampul terkenal dengan hasil pertaniannya yang melimpah serta terdapat beberapa ahli pengrajin tangan, sehingga banyak sekali produk yang dihasil dan dikaryakan.")

        tvContentTwo.setText("Produk unggulan dari UMKM di Desa Duwet adalah sebuah koleksi kerajinan tangan yang memukau. Diproduksi dengan penuh keahlian oleh para ahli pengrajin lokal, produk – produk ini mencerminkan keindahan alam dan kekayaan budaya Desa Duwet. Dengan sentuhan kreativitas dan kualitas yang terjaga, setiap produk membawa cerita dan nilai – nilai luhur dari masyarakat setempat. Dari perhiasan etnik yang elegan hingga tekstil khas dengan motif tradisional yang memukau, produk – produk UMKM kami siap memperkaya pengalaman anda dalam mengenali kekayaan Desa Duwet. Mari bergabung dan dukung perkembangan UMKM lokal dengan memilih produk berkualitas dan berdaya saing dari Desa Duwet!")

        return view
    }

    fun init(view: View){
        vpSlider = view.findViewById(R.id.vp_slider_about)
        tvContent = view.findViewById(R.id.content_about)
        tvContentTwo = view.findViewById(R.id.content_about_two)
    }
}