PRO401 – Semana 6 · App Android + Firebase Auth + GPS + Realtime DB

Aplicación Android (Kotlin, minSdk 23) que:

Registra/inicia sesión con Firebase Authentication (Email/Password).

Obtiene la ubicación (lat/lng) con FusedLocationProviderClient.

Guarda la última ubicación en Firebase Realtime Database en users/{uid}/lastLocation.

Repositorio: [PEGA_AQUÍ_LA_URL_DEL_REPO]
Autor(es): [TU(S) NOMBRE(S)]

Requisitos

Android Studio (SDK 23+).

Proyecto Firebase y archivo app/google-services.json.

Configuración Firebase

Crear proyecto y registrar la app con tu applicationId.

Authentication → Sign-in method → Email/Password → Enable.

Realtime Database → Crear (us-central1 recomendado) y publicar reglas:
{ "rules": { ".read": "auth != null", ".write": "auth != null" } }

Permisos (AndroidManifest.xml)
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

Dependencias clave (app/build.gradle.kts)
implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-database")
implementation("com.google.android.gms:play-services-location:21.3.0")
implementation("androidx.appcompat:appcompat:1.7.0")

Cómo ejecutar

Clonar el repo y abrir en Android Studio.

Copiar google-services.json a app/.

Ejecutar en emulador API 23 y API 26+.

En LoginActivity crear cuenta o iniciar sesión → se navega a MenuActivity.

Verás la ubicación y el toast de guardado en la nube.

Evidencias (capturas)

Sube tus imágenes a docs/capturas/ con estos nombres para que se vean aquí.

Login con error controlado:


Menú con lat/lng y confirmación de guardado:


Realtime Database con nodos por UID:


Authentication con usuarios/UIDs:


Historias de usuario (resumen)

HU-01: Registro/Login con email y contraseña.

HU-02: Solicitud de permisos de ubicación (runtime).

HU-03: Visualización de lat/lng en pantalla.

HU-04: Guardado de ubicación en Realtime Database.

HU-05: Reglas de seguridad (auth != null).

HU-06: Pruebas en API 23 y API 26

Estructura de datos
users/
  {uid}/
    lastLocation:
      lat: double
      lng: double
      timestamp: long

Notas

Se usa minSdk 23 porque las versiones actuales de Firebase Auth requieren Android 6.0+.

google-services.json se incluye solo con fines académicos en esta entrega.
