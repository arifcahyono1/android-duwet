package com.cahyono.tokoonline.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lll")
data class Name(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    var name: String,
    var belakang: String
) {
    constructor(name: String, belakang: String) : this(null, name, belakang)
}