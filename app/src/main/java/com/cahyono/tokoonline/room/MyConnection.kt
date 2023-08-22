package com.cahyono.tokoonline.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cahyono.tokoonline.data.Name
import com.cahyono.tokoonline.data.NameDao
import com.cahyono.tokoonline.model.Alamat
import com.cahyono.tokoonline.model.Keranjang
import com.cahyono.tokoonline.model.Produk

@Database(entities = [Alamat::class] /* List model Ex:NoteModel */, version = 1)
abstract class MyConnection : RoomDatabase() {
    abstract fun daoAlamat(): DaoAlamat

    companion object {
        private var INSTANCE: MyConnection? = null

        fun getInstance(context: Context): MyConnection? {
            if (INSTANCE == null) {
                synchronized(MyConnection::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyConnection::class.java, "MyDbalamat" // Database Name
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}