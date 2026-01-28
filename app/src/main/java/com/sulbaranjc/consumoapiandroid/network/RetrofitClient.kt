package com.sulbaranjc.consumoapiandroid.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://clientes-api.docker.sulbaranjc.com/"

    // Configurar OkHttpClient con Logging Interceptor
    // Esto permite ver las peticiones y respuestas HTTP en Logcat
    // Nivel BODY: muestra headers + cuerpo completo (ideal para debugging)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Añadir cliente con logging configurado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

