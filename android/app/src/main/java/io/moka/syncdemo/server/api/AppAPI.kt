package io.moka.syncdemo.server.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import io.moka.syncdemo.BuildConfig
import io.moka.syncdemo.server.ServerInfo
import io.moka.syncdemo.server.request.SyncServerReq
import io.moka.syncdemo.server.response.SyncLocalRes
import io.moka.syncdemo.server.response.SyncServerRes
import io.moka.syncdemo.util.TestUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


object AppAPI {

    val api: API by lazy { retrofit.create(API::class.java) }

    private val endpoint = ServerInfo.appEndPoint

    private val client by lazy {
        OkHttpClient.Builder()
                .addInterceptor(initHttpLoggingInterceptor())
                .apply {
                    if (TestUtil.isDebugMode)
                        addNetworkInterceptor(StethoInterceptor())
                }
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .client(client
                        .newBuilder()
                        .addInterceptor { chain ->
                            chain.proceed(
                                    chain.request()
                                            .newBuilder()
                                            .addHeader("Accept", "application/vnd.syncdemo.v1+json")
                                            .addHeader("Content-Type", "application/json")
                                            .build()
                            )
                        }
                        .build()
                )
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build()!!
    }

    private fun initHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (TestUtil.isDebugMode)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    interface API {

        @GET("/v1/api/syncLocal")
        fun syncLocal(@Query("syncAt") syncAt: Long): Call<SyncLocalRes>

        @POST("/v1/api/syncServer")
        fun syncServer(@Body syncServerReq: SyncServerReq): Call<SyncServerRes>

    }

}
