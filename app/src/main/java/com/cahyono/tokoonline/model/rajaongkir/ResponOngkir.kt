package com.cahyono.tokoonline.model.rajaongkir

import com.cahyono.tokoonline.model.ModelAlamat

class ResponOngkir {
    val rajaongkir = Rajaongkir()

    class Rajaongkir{
        val results = ArrayList<Result>()
    }

}