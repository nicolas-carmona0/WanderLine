package com.ncjappdev.wanderline.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.util.UUID

@Entity(tableName = "routes")
data class WanderLineItem (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val name: String,
    val day: Int,
    val latitude: Double,
    val longitude: Double,
    val orderIndex: Int = 0
)
