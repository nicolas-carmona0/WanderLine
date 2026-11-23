package com.ncjappdev.wanderline.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ncjappdev.wanderline.model.WanderLineItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WanderLineDao {

    @Query("SELECT * FROM routes ORDER BY day ASC, orderIndex ASC")
    fun getAllRoutes(): Flow<List<WanderLineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(item: WanderLineItem)

    @Update
    suspend fun updateRoute(item: WanderLineItem)

    @Query("DELETE FROM routes WHERE id = :itemId")
    suspend fun deleteRoutesById(itemId: String)

}