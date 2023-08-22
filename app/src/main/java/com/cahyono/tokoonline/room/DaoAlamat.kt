package com.cahyono.tokoonline.room

import androidx.room.*
import com.cahyono.tokoonline.model.Alamat
import com.cahyono.tokoonline.model.Produk

@Dao
interface DaoAlamat {

    @Insert
    fun insert(data: Alamat)

    @Delete
    fun delete(data: Alamat)

    @Update
    fun update(data: Alamat): Int

    @Query("SELECT * from alamat ORDER BY id ASC")
    fun getAll(): List<Alamat>
//
//    @Query("SELECT * FROM alamat WHERE id = :id LIMIT 1")
//    fun getProduk(id: Int): Alamat
//
    @Query("SELECT * FROM alamat WHERE isSelected = :status ORDER BY id DESC LIMIT 1")
    fun getByStatus(status: Boolean): Alamat?
//
//    @Query("DELETE FROM alamat")
//    fun deleteAll(): Int
}