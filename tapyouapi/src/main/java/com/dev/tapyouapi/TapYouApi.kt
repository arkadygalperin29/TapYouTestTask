package com.dev.tapyouapi

import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface TapYouApi {
    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") amountOfDots: Int): PointsResponse<PointDto>
}


fun TapYouApi(
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): TapYouApi {
    return retrofit(baseUrl, okHttpClient, json).create(TapYouApi::class.java)
}


private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient?,
    json: Json,
): Retrofit {

    val jsonConverterFactory =
        json.asConverterFactory(contentType = "application/json".toMediaType())

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    val modifiedOkhttpClient: OkHttpClient =
        (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
            .addInterceptor(loggingInterceptor)
            .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(modifiedOkhttpClient)
        .addConverterFactory(jsonConverterFactory)
        .run { if (okHttpClient != null) client(okHttpClient) else this }
        .build()
    return retrofit
}