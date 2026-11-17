# Destinos Colombia — Android nativo

Aplicación Android desarrollada en **Kotlin + Jetpack Compose**. Esta versión reemplaza el proyecto original NativeScript/Angular y se abre directamente desde Android Studio.

## Funcionalidades
- Inicio con presentación de la aplicación y destinos destacados.
- Catálogo de seis destinos: Cartagena, Guatapé, Salento, Parque Tayrona, San Andrés y Medellín.
- Búsqueda por nombre, región o descripción.
- Pantalla de detalle con descripción y actividades.
- Favoritos compartidos entre las pantallas de inicio, exploración y detalle.
- Navegación inferior entre Inicio, Explorar y Favoritos.

## Requisitos
- Android Studio actualizado.
- JDK 17 (Android Studio puede usar su JBR integrado).
- Android SDK 35 instalado desde SDK Manager.
- Gradle 8.9 o compatible con Android Gradle Plugin 8.7.3 (Android Studio descarga Gradle al sincronizar).

## Abrir y ejecutar
1. Descomprime el proyecto.
2. En Android Studio selecciona **Open**.
3. Selecciona la carpeta raíz `DestinosColombiaKotlin` (la que contiene `settings.gradle.kts`).
4. Acepta **Sync Project with Gradle Files** y espera la descarga de dependencias.
5. Abre **Tools > Device Manager**, crea/inicia un emulador (API 24 o superior).
6. Selecciona el emulador y pulsa **Run ▶**.

## Identificador
`com.sebas.destinoscolombia`

## Nota
Los datos se mantienen en memoria mientras la aplicación está abierta. Los favoritos no se guardan todavía después de cerrar la app. Las ilustraciones de destinos usan emojis y fondos de color; no requieren conexión a internet.
