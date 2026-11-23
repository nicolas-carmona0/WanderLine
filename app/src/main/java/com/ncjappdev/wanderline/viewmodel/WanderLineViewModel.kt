package com.ncjappdev.wanderline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncjappdev.wanderline.data.WanderLineRepository
import com.ncjappdev.wanderline.model.WanderLineItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WanderLineViewModel @Inject constructor(
    private val repository: WanderLineRepository
) : ViewModel() {

    // Leemos de la base de datos automáticamente. Se actualiza solo si hay cambios
    val routes = repository.getAllRoutes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estado interno (mutable) protegido para que solo el VM pueda modificarlo
    private val _routes = MutableStateFlow<List<WanderLineItem>>(emptyList())

    // Agregar nueva ruta
    fun addRoute(item: WanderLineItem) {
        // Añadimos el item completo a la lista actual
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRoute(item)
        }
    }
    // Actualizar una ruta existente
    fun updateRoute(updatedItem: WanderLineItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRoute(updatedItem)
        }
    }

    // Borrar una ruta recibiendo solo su ID
    fun deleteRouteById(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRouteById(id)
        }
    }

    // Intercambiar posiciones ruta del mismo día
    fun moveRoute(dayRoutes: List<WanderLineItem>, fromIndex: Int, toIndex: Int) {
        if (toIndex !in dayRoutes.indices) return

        viewModelScope.launch {
            // 1. Hacemos una copia mutable de la lista del día y movemos el elemento
            val mutableList = dayRoutes.toMutableList()
            val movedItem = mutableList.removeAt(fromIndex)
            mutableList.add(toIndex, movedItem)

            // 2. Actualizamos TODOS los elementos de ese día con su nuevo índice limpio (0, 1, 2...)
            mutableList.forEachIndexed { newIndex, item ->
                if (item.orderIndex != newIndex) {
                    repository.updateRoute(item.copy(orderIndex = newIndex))
                }
            }
        }
    }

}