package com.ncjappdev.wanderline

/*

2. WanderLine: Itinerario de viaje "Offline-First" con Mapas
El Concepto: Un planificador visual de viajes. El usuario crea un viaje (ej. "Roma 2026") y va
añadiendo lugares para visitar cada día. La pantalla principal divide el viaje por días usando
cabeceras pegajosas (sticky headers) y muestra un mapa interactivo arriba que cambia los pines en
tiempo real según el día que estés mirando en la lista.

El Reto Técnico para tu CV (Nivel Intermedio-Alto):

Google Maps Compose SDK: Integrar mapas directamente con la librería oficial de Compose para dibujar
marcadores y rutas dinámicas.

Listas Complejas: Dominarás el LazyColumn a un nivel superior implementando stickyHeader (para que el
título "Día 1", "Día 2" se quede anclado arriba al hacer scroll) y la función de arrastrar elementos
para reordenar la ruta (drag to reorder).

Single Source of Truth: Manejar un estado complejo donde al tocar un elemento de la lista, la cámara
del mapa se anime y haga zoom al punto exacto de forma fluida.

Potencial en Play Store: El gran problema de los viajeros es quedarse sin datos (roaming) en otro país.
Si construyes esta app basándote en Room (como ya sabes hacer con SubScout) para que el itinerario sea
100% accesible sin conexión a internet, tendrás un valor diferencial enorme frente a otras apps web.

*/

/*

    - Saber información locales maps al pulsar
    - Camino rutas


*/