# 📡 Logging Interceptor - Guía para Debugging de API

## 🎯 ¿Qué es el Logging Interceptor?

Es una herramienta de **OkHttp** que permite **ver en Logcat** todas las peticiones HTTP que hace tu app y las respuestas que recibe del servidor.

---

## 🔧 Configuración (Ya está hecho en el proyecto)

En `RetrofitClient.kt` está configurado así:

```kotlin
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )
    .build()
```

---

## 📊 Niveles de Logging

| Nivel | Qué muestra |
|-------|-------------|
| `NONE` | Nada (para producción) |
| `BASIC` | Método + URL + código de respuesta |
| `HEADERS` | BASIC + headers de request/response |
| `BODY` | HEADERS + cuerpo completo ⭐ **Usado en este proyecto** |

---

## 👁️ Cómo ver los logs en Android Studio

### Paso 1: Abrir Logcat
1. Ejecuta la app en el emulador o dispositivo
2. En Android Studio, ve a la pestaña **"Logcat"** (abajo)

### Paso 2: Filtrar logs de OkHttp
En el campo de búsqueda de Logcat, escribe:
```
OkHttp
```

---

## 📝 Ejemplo de lo que verás en Logcat

### Al hacer GET /clientes:

```logcat
D/OkHttp: --> GET http://clientes-api.docker.sulbaranjc.com/clientes
D/OkHttp: --> END GET
D/OkHttp: <-- 200 OK http://clientes-api.docker.sulbaranjc.com/clientes (142ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: 
D/OkHttp: [
D/OkHttp:   {
D/OkHttp:     "id": 1,
D/OkHttp:     "nombre": "Juan",
D/OkHttp:     "apellido": "Pérez",
D/OkHttp:     "email": "juan@example.com",
D/OkHttp:     "telefono": "+34612345678",
D/OkHttp:     "direccion": "Calle Mayor 10, Madrid"
D/OkHttp:   },
D/OkHttp:   ...
D/OkHttp: ]
D/OkHttp: <-- END HTTP (234-byte body)
```

### Al hacer POST /clientes:

```logcat
D/OkHttp: --> POST http://clientes-api.docker.sulbaranjc.com/clientes
D/OkHttp: Content-Type: application/json; charset=UTF-8
D/OkHttp: Content-Length: 156
D/OkHttp: 
D/OkHttp: {
D/OkHttp:   "nombre": "María",
D/OkHttp:   "apellido": "García",
D/OkHttp:   "email": "maria@example.com",
D/OkHttp:   "telefono": "+34655887766",
D/OkHttp:   "direccion": "Avenida Principal 25, Barcelona"
D/OkHttp: }
D/OkHttp: --> END POST (156-byte body)
D/OkHttp: 
D/OkHttp: <-- 201 Created http://clientes-api.docker.sulbaranjc.com/clientes (89ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: 
D/OkHttp: {
D/OkHttp:   "id": 5,
D/OkHttp:   "nombre": "María",
D/OkHttp:   "apellido": "García",
D/OkHttp:   "email": "maria@example.com",
D/OkHttp:   "telefono": "+34655887766",
D/OkHttp:   "direccion": "Avenida Principal 25, Barcelona"
D/OkHttp: }
D/OkHttp: <-- END HTTP (178-byte body)
```

### Al tener un ERROR 404:

```logcat
D/OkHttp: --> PUT http://clientes-api.docker.sulbaranjc.com/clientes/999
D/OkHttp: Content-Type: application/json; charset=UTF-8
D/OkHttp: 
D/OkHttp: {
D/OkHttp:   "nombre": "Cliente",
D/OkHttp:   "apellido": "Inexistente"
D/OkHttp: }
D/OkHttp: --> END PUT
D/OkHttp: 
D/OkHttp: <-- 404 Not Found http://clientes-api.docker.sulbaranjc.com/clientes/999 (45ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: 
D/OkHttp: {
D/OkHttp:   "detail": "Cliente no encontrado"
D/OkHttp: }
D/OkHttp: <-- END HTTP
```

---

## 🐛 Debugging con Logging

### Problema 1: "Mi POST no funciona"

**Sin Logging:**
```
❌ "onFailure: Error desconocido"
```

**Con Logging:**
```
✅ Ves el JSON exacto que enviaste
✅ Ves el código de error (400, 422, 500)
✅ Ves el mensaje de error del servidor
```

### Problema 2: "No me trae datos"

**Sin Logging:**
```
❌ Lista vacía, no sabes por qué
```

**Con Logging:**
```
✅ Ves si la petición se hizo
✅ Ves el código de respuesta (200, 500)
✅ Ves si el servidor devuelve [] o un error
```

---

## 🎓 Casos de Uso Pedagógicos

### 1. Ver qué campos se envían en POST/PUT
Los alumnos pueden **verificar** si están enviando:
- Los campos correctos
- El formato JSON correcto
- Valores null vs valores vacíos

### 2. Entender los códigos HTTP
- `200 OK` - Éxito
- `201 Created` - Recurso creado
- `400 Bad Request` - Datos inválidos
- `404 Not Found` - Recurso no existe
- `422 Unprocessable Entity` - Validación falló
- `500 Internal Server Error` - Error del servidor

### 3. Medir tiempos de respuesta
```
<-- 200 OK (142ms)  ← Ver cuánto tarda cada petición
```

### 4. Detectar problemas de red
Si no ves ningún log de OkHttp, puede ser:
- URL incorrecta
- Sin conexión a internet
- Firewall bloqueando

---

## ⚠️ Importante para Producción

**NUNCA** uses `Level.BODY` en producción porque:
- Expone datos sensibles en logs
- Consume recursos innecesarios
- Puede mostrar contraseñas, tokens, etc.

**Para producción:** Cambiar a `Level.NONE` o quitar el interceptor completamente.

---

## 🎯 Ejercicio para Alumnos

1. Ejecuta la app
2. Abre Logcat y filtra por "OkHttp"
3. Haz una petición (listar clientes, agregar uno, etc.)
4. **Observa y anota:**
   - ¿Qué método HTTP se usó? (GET, POST, PUT, DELETE)
   - ¿Cuál fue el código de respuesta?
   - ¿Cuánto tiempo tardó?
   - ¿Qué datos se enviaron?
   - ¿Qué datos se recibieron?

---

## 📚 Recursos Adicionales

- [Documentación oficial de OkHttp Logging](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
- [Códigos HTTP explicados](https://developer.mozilla.org/es/docs/Web/HTTP/Status)

---

**💡 Consejo del Profesor:**
> "El Logging Interceptor es un momento pedagógico brutal para enseñar debugging de API.  
> Los alumnos VEN exactamente qué pasa en cada petición."

🎓 **¡Aprovecha esta herramienta para aprender y enseñar mejor!**

