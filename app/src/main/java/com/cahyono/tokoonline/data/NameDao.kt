package com.cahyono.tokoonline.data

import androidx.room.*
import com.cahyono.tokoonline.model.Produk

@Dao
interface NameDao {
    @Query("SELECT * FROM name")
    fun getAll(): List<Produk>

    @Insert
    fun insertAll(name: Produk)

    @Delete
    fun delete(user: Produk)

    @Query("DELETE FROM name WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("SELECT * FROM name WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Produk

    @Update
    fun update(data: Produk): Int
}