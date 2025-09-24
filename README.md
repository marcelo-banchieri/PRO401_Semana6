# PRO401 – Semana 6: App Android + Firebase Auth + GPS + Realtime DB

Aplicación Android (Kotlin, **minSdk 23**) que:
- Registra / inicia sesión con **Firebase Authentication (email/clave)**.
- Obtiene la ubicación con **FusedLocationProviderClient**.
- Guarda la última ubicación en **Firebase Realtime Database** en `users/{uid}/lastLocation`.

## Requisitos
- Android Studio (SDK 23+)
- Proyecto Firebase configurado y `app/google-services.json` presente.

## Configuración Firebase
1. Crear proyecto en Firebase y **registrar** la app con `applicationId = com.marcelo.distribuidoraapp3`.
2. **Authentication → Sign-in method → Email/Password → Enable**.
3. **Realtime Database → Crear** (ej. `us-central1`) y reglas:
   ```json
   { "rules": { ".read": "auth != null", ".write": "auth != null" } }
