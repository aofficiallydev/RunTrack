package com.aofficially.runtrack.di.network

import android.content.Context
import com.aofficially.runtrack.BuildConfig
import com.aofficially.runtrack.di.network.interceptor.InterceptorModule
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_CUSTOM_DOMAIN
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module(
    includes = [
        InterceptorModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitInterface(okHttpClient: OkHttpClient, pref: PreferenceDataSource): Retrofit {
        val gsonBuilder = GsonBuilder().setLenient()
        val gson = gsonBuilder.create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(
            Schedulers.io()
        )

        return Retrofit.Builder()
            .baseUrl(pref.getString(KEY_CUSTOM_DOMAIN, NetworkConfig.BASE_URL) + "api/PlaceInfo/")
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Inject
    @Provides
    fun provideOkHttp(
        interceptors: Interceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return provideOkHttpBuilder(interceptors, context).build()
    }

    private fun provideOkHttpBuilder(
        interceptors: Interceptor, context: Context
    ): OkHttpClient.Builder {
        val okHttpBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        val chunkerCollector = ChuckerCollector(
            context = context,                 // Context on which you are
            showNotification = true,        // Boolean for showing Notification, set to true to show and false otherwise
            retentionPeriod = RetentionManager.Period.ONE_WEEK  // Period taken to retain the collected data, can be an hour, day or week
        )

        val chunkerInterceptor = ChuckerInterceptor.Builder(context) // `this` is the context
            .collector(chunkerCollector)
            .maxContentLength(150_000L)
            .alwaysReadResponseBody(true)
            .build()

        okHttpBuilder.apply {
            addInterceptor(logging)
            addInterceptor(interceptors)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(chunkerInterceptor)
//                addInterceptor(SSLHandshakeInterceptor())
            }

//            if (BuildConfig.isSecureRequire) {
//                certificatePinner(getCertificateByFlavor())
//            }
        }
        return okHttpBuilder
    }

//    private fun getCertificateByFlavor(): CertificatePinner {
//        val flavor = BuildConfig.FLAVOR
//        return when (Environments.values().firstOrNull { it.profile == flavor }) {
//            Environments.PROD -> provideProdCertificatePinning()
//            else -> provideUatCertificatePinning() // stubby on local
//        }
//    }
//
//    private fun provideUatCertificatePinning(): CertificatePinner {
//        val hostname = "*.uat.krungthai.com"
//        return CertificatePinner.Builder()
//            .add(hostname, "sha256/uq+k1NvZmc4yk38JtvBd+9SiAJ9JBcs8yR1sjsppnV4=")
//            .add(hostname, "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=")
//            .add(hostname, "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=")
//            .build()
//    }
//
//    private fun provideProdCertificatePinning(): CertificatePinner {
//        val hostname = "*.krungthai.com"
//        return CertificatePinner.Builder()
//            .add(hostname, "sha256/Iu/zsAmxr67pawLxhwrfZL3scIgjBzzF25OoC7L/3wc=")
//            .add(hostname, "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=")
//            .add(hostname, "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=")
//            .build()
//    }

//    private fun getUnsafeOkHttpClient(okHttpClient: OkHttpClient): OkHttpClient.Builder {
//        return try {
//            // Create a trust manager that does not validate certificate chains
//            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
//                @Throws(CertificateException::class)
//                override fun checkClientTrusted(
//                    chain: Array<X509Certificate?>?,
//                    authType: String?
//                ) {
//                }
//
//                @Throws(CertificateException::class)
//                override fun checkServerTrusted(
//                    chain: Array<X509Certificate?>?,
//                    authType: String?
//                ) {
//                }
//
//                override fun getAcceptedIssuers(): Array<X509Certificate> {
//                    return arrayOf()
//                }
//            }
//            )
//
//            // Install the all-trusting trust manager
//            val sslContext: SSLContext = SSLContext.getInstance("SSL")
//            sslContext.init(null, trustAllCerts, SecureRandom())
//
//            // Create an ssl socket factory with our all-trusting manager
//            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
//            val builder = okHttpClient.newBuilder()
//            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//            builder.hostnameVerifier { hostname, session -> true }
//            builder
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }

//    enum class Environments(val profile: String) {
//        DEV("dev"),
//        SIT("sit"),
//        UAT("uat"),
//        PROD("production")
//    }

    companion object {
        private const val TIMEOUT = 30L
    }
}