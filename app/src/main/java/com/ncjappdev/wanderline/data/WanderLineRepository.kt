package com.ncjappdev.wanderline.data

import com.ncjappdev.wanderline.model.WanderLineItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WanderLineRepository @Inject constructor(
    private val dao: WanderLineDao
){

    fun getAllRoutes(): Flow<List<WanderLineItem>>{
        return dao.getAllRoutes()
    }

    suspend fun insertRoute(item: WanderLineItem){
        dao.insertRoute(item)
    }

    suspend fun updateRoute(item: WanderLineItem){
        dao.updateRoute(item)
    }

    suspend fun deleteRouteById(itemId: String){
        dao.deleteRoutesById(itemId)
    }

}