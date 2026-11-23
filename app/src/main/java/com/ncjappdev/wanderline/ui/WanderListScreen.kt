package com.ncjappdev.wanderline.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ncjappdev.wanderline.model.WanderLineItem
import com.ncjappdev.wanderline.viewmodel.WanderLineViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WanderListScreen(
    wanderViewModel: WanderLineViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    var editingId by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var dayStr by remember { mutableStateOf("") }
    var latitudeStr by remember { mutableStateOf("") }
    var longitudeStr by remember { mutableStateOf("") }


    var isSheetVisible by remember { mutableStateOf(false) }

    val routesList by wanderViewModel.routes.collectAsStateWithLifecycle()

    val uniqueDays = routesList.map { it.day }.distinct().sorted()
    val pagerState = rememberPagerState(pageCount = { uniqueDays.size })

    val currentVisibleDay = if (uniqueDays.isNotEmpty()) {
        uniqueDays[pagerState.currentPage]
    } else {
        -1
    }


    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Comprobamos si nos han dado el permiso preciso o el aproximado
        isLocationPermissionGranted =
            permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // Lanzamos la petición nada más abrir la pantalla
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Definimos unas coordenadas de prueba
    val baseCoordinates = LatLng(41.902782, 12.496366)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(baseCoordinates, 12f)
    }


    // Centrar mapa en primer elemento del día
    LaunchedEffect(currentVisibleDay) {
        // 1. Buscamos el primer destino del día actual
        val firstDestination = routesList.firstOrNull { it.day == currentVisibleDay }

        // 2. Si existe, vamos hacia él
        if (firstDestination != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(firstDestination.latitude, firstDestination.longitude),
                    14f // nivel de zum
                ),
                durationMs = 1000
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    // Todo de momento no se que haré con esto
                    Text("WanderLine")
                }
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    isSheetVisible = true
//                }
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Añadir")
//            }
//        },
//        floatingActionButtonPosition = FabPosition.Start
    ) { innerPadding ->

        if (isSheetVisible) {
            ModalBottomSheet(onDismissRequest = { isSheetVisible = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = dayStr,
                        onValueChange = { dayStr = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Día") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(
                            onClick = {
                                // Guardar datos
                                val parsedDay = dayStr.toIntOrNull() ?: 1

                                val newRoute = WanderLineItem(
                                    id = editingId ?: UUID.randomUUID().toString(),
                                    name = name,
                                    day = parsedDay,
                                    latitude = selectedLocation?.latitude ?: 0.0,
                                    longitude = selectedLocation?.longitude ?: 0.0
                                )

                                if (editingId != null) {
                                    wanderViewModel.updateRoute(newRoute)
                                } else {
                                    wanderViewModel.addRoute(newRoute)
                                }

                                name = ""
                                dayStr = ""
                                latitudeStr = ""
                                longitudeStr = ""

                                isSheetVisible = false
                            },
                            enabled = name.isNotBlank() && dayStr.isNotBlank()
                        ) {
                            Icon(Icons.Filled.Save, contentDescription = "Save")
                        }
                    }

                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 3. Pintamos el mapa ocupando todo el espacio disponible
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    selectedLocation = latLng
                    isSheetVisible = true
                },
                properties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted)
            ) {
                routesList.filter { it.day == currentVisibleDay }.forEach { route ->
                    Marker(
                        state = MarkerState(position = LatLng(route.latitude, route.longitude)),
                        title = route.name
                    )
                }
            }
            if (routesList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no hay paradas en este viaje")
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->

                    val currentDay = uniqueDays[page]
                    val destinationsForThisPage = routesList.filter { it.day == currentDay }

                    Column(modifier = Modifier.fillMaxSize()) {

                        // Cabecera día
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "\uD83D\uDCCD Día $currentDay",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(Modifier.weight(1f))
                            Button(
                                onClick = {
                                    if (destinationsForThisPage.isNotEmpty()) {
                                        // 1. El destino final es el ÚLTIMO elemento de la lista del día
                                        val destination = destinationsForThisPage.last()
                                        val destString =
                                            "${destination.latitude},${destination.longitude}"

                                        // 2. Si hay más de 1 parada, cogemos todas MENOS la última para hacer de paradas intermedias
                                        val waypointsString =
                                            if (destinationsForThisPage.size > 1) {
                                                destinationsForThisPage
                                                    .dropLast(1) // Quitamos el destino final
                                                    .joinToString("|") { "${it.latitude},${it.longitude}" } // Las unimos con '|'
                                            } else {
                                                ""
                                            }

                                        // 3. Construimos la URL oficial
                                        val uriString = if (waypointsString.isNotEmpty()) {
                                            "https://www.google.com/maps/dir/?api=1&destination=$destString&waypoints=$waypointsString&travelmode=walking"
                                        } else {
                                            "https://www.google.com/maps/dir/?api=1&destination=$destString&travelmode=walking"
                                        }

                                        // 4. Lanzamos Google Maps
                                        val mapIntent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        context.startActivity(mapIntent)
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.Route, contentDescription = "Iniciar ruta")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ruta completa")
                            }
                        }


                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Tarjetas de ese día
                            itemsIndexed(
                                items = destinationsForThisPage,
                                key = { _, route -> route.id}
                            ) { index, route ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                        .clickable {
                                            coroutineScope.launch {
                                                cameraPositionState.animate(
                                                    update = CameraUpdateFactory.newLatLngZoom(
                                                        LatLng(route.latitude, route.longitude),
                                                        14f
                                                    ),
                                                    durationMs = 1000
                                                )
                                            }
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            IconButton(
                                                onClick = { wanderViewModel.moveRoute(destinationsForThisPage, index, index - 1) },
                                                enabled = index > 0
                                            ) {
                                                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Subir parada")
                                            }
                                            IconButton(
                                                onClick = { wanderViewModel.moveRoute(destinationsForThisPage, index, index + 1) },
                                                enabled = index < destinationsForThisPage.lastIndex
                                            ) {
                                                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Bajar parada")
                                            }
                                        }

                                        Text(
                                            text = "${index + 1}. ${route.name}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 8.dp)
                                        )

                                        IconButton(
                                            onClick = { wanderViewModel.deleteRouteById(id = route.id) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DeleteForever,
                                                contentDescription = "Delete"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
