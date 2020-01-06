package com.mayandro.waterio.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    var id: String,

    @ColumnInfo(name = "user_phone")
    @SerializedName("user_phone")
    var phone: String,

    @ColumnInfo(name = "user_created_at")
    @SerializedName("user_created_at")
    var createdAt: String,

    @ColumnInfo(name = "user_last_used")
    @SerializedName("user_last_used")
    var lastGaolReached: String,

    @ColumnInfo(name = "user_current_streak")
    @SerializedName("user_current_streak")
    var currentStreak: Int,

    @ColumnInfo(name = "user_longest_streak")
    @SerializedName("user_longest_streak")
    var longestStreak: Int,

    @ColumnInfo(name = "user_daily_goal")
    @SerializedName("user_daily_goal")
    var dailyGoal: Float



)