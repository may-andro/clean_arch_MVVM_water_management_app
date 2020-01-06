package com.mayandro.waterio.data.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class UserLog(
    @ColumnInfo(name = "log_time")
    @SerializedName("log_time")
    val logTime: String,

    @ColumnInfo(name = "log_quantity")
    @SerializedName("log_quantity")
    val logQuantity: Float
)