package com.ncjappdev.wanderline.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ncjappdev.wanderline.model.WanderLineItem

@Database(entities = [WanderLineItem::class], version = 2, exportSchema = false)
abstract class WanderLineDatabase : RoomDatabase(){
    abstract val dao: WanderLineDao
}