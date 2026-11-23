# 🗺️ WanderLine

**WanderLine** es una aplicación nativa para Android diseñada para planificar itinerarios de viaje por días de forma visual, rápida e intuitiva. Permite marcar puntos de interés directamente sobre un mapa interactivo, organizar las paradas por jornadas, reordenar la secuencia de visita y lanzar la ruta completa paso a paso en Google Maps.

---

## ✨ Características principales

* **Creación intuitiva sobre el mapa:** Añade paradas mediante una pulsación larga (`onMapLongClick`) directamente en la ubicación deseada, evitando introducir coordenadas manualmente.
* **Organización paginada por días:** Navegación horizontal fluida mediante `HorizontalPager` sincronizada en tiempo real con los marcadores visibles en el mapa y el enfoque automático de la cámara (`LaunchedEffect`).
* **Reordenamiento dinámico de rutas:** Modifica el orden de las paradas dentro de cada día de forma persistente (`orderIndex` en Room), actualizando automáticamente el punto de partida y la secuencia del itinerario.
* **Geolocalización en tiempo real:** Gestión de permisos de ubicación en tiempo de ejecución (`ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`) para visualizar la posición actual del usuario en el mapa.
* **Navegación multi-parada con Google Maps:** Generación dinámica de *Intents* explícitos con soporte para múltiples puntos intermedios (`waypoints`) que abren la ruta completa a pie directamente en la aplicación oficial de Google Maps.

---

## 🛠️ Stack Tecnológico y Arquitectura

El proyecto sigue las recomendaciones modernas de desarrollo en Android bajo el patrón arquitectónico **MVVM (Model-View-ViewModel)** y flujo de datos unidireccional:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material Design 3
* **Mapas y Ubicación:** Google Maps SDK for Android (`maps-compose`)
* **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room) con consultas reactivas mediante `Flow`
* **Inyección de Dependencias:** [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) + KSP
* **Asincronía y Estado:** Kotlin Coroutines & `StateFlow` (`collectAsStateWithLifecycle`)

---

## 📂 Estructura del Proyecto

```text
com.nicolascarmona.wanderline
├── data
│   ├── local          # Entidades (WanderLineItem), DAO y RoomDatabase
│   └── repository     # Abstracción e implementación del acceso a datos
├── di                 # Módulos de inyección de dependencias con Hilt (DatabaseModule)
├── ui
│   ├── theme          # Configuración de colores, tipografía y tema Material 3
│   └── WanderListScreen.kt  # Pantalla principal (GoogleMap + HorizontalPager + BottomSheet)
└── viewmodel          # WanderLineViewModel (gestión de estado y lógica de reordenamiento)
