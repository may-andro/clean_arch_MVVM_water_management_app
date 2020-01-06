package com.mayandro.waterio.data.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity(tableName = "user_history_table")
data class UserHistory(
    @PrimaryKey
    @ColumnInfo(name = "history_date")
    @SerializedName("history_date")
    var historyDate: String,

    @ColumnInfo(name = "history_quantity")
    @SerializedName("history_quantity")
    var historyQuantity: Float,

    @ColumnInfo(name = "history_goal")
    @SerializedName("history_goal")
    var historyGoal: Float,

    @ColumnInfo(name = "history_user_phone")
    @SerializedName("history_user_phone")
    var historyUserPhone: String,

    @ColumnInfo(name = "history_log")
    @SerializedName("history_log")
    val historyLog: MutableList<UserLog>
)


class DataConverter {
    @TypeConverter
    fun fromUserLogList(usrLogList: List<UserLog>?): String? {
        usrLogList?.let {
            val type: Type = object : TypeToken<List<UserLog>?>() {}.type
            return Gson().toJson(it, type)
        } ?: kotlin.run { return null }
    }

    @TypeConverter
    fun toUserLogList(userLogString: String?): List<UserLog>? {
        userLogString?.let {
            val type: Type = object : TypeToken<List<UserLog>?>() {}.type
            return Gson().fromJson(it, type)
        } ?: kotlin.run { return null }
    }
}