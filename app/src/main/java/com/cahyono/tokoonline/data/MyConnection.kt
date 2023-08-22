package com.cahyono.tokoonline.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cahyono.tokoonline.model.Produk
import com.cahyono.tokoonline.room.MyDatabase

@Database(entities = [Produk::class], version = 1)
abstract class MyConnection : RoomDatabase() {
    abstract fun NameDao(): NameDao

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, "MyDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}