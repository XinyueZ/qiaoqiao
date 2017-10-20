package com.qiaoqiao.repository.backend

import android.content.Context
import android.text.TextUtils
import com.google.gson.*
import com.qiaoqiao.R
import com.qiaoqiao.app.Key
import com.qiaoqiao.repository.annotation.RepositoryScope
import com.qiaoqiao.repository.backend.model.wikipedia.Page
import com.qiaoqiao.repository.backend.model.wikipedia.Pages
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

@Module
class BackendModule {

    @RepositoryScope
    @Provides
    internal fun provideGoogle(cxt: Context, key: Key): Google {
        return Google(cxt, key)
    }

    @RepositoryScope
    @Provides
    internal fun provideKnowledge(cxt: Context): Wikipedia {

        val r = Retrofit.Builder().baseUrl(cxt.getString(R.string.base_url_knowledge))
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return r.create(Wikipedia::class.java)
    }

    @RepositoryScope
    @Provides
    internal fun provideProductService(cxt: Context): ProductsService {

        val r = Retrofit.Builder().baseUrl(cxt.getString(R.string.base_url_knowledge)).client(createUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return r.create(ProductsService::class.java)
    }

    @RepositoryScope
    @Provides
    internal fun provideImageProvider(cxt: Context): ImageProvider {
        val r = Retrofit.Builder().baseUrl(cxt.getString(R.string.base_url_knowledge))
                .client(OkHttpClient().newBuilder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return r.create(ImageProvider::class.java)
    }

    companion object {
        internal val GSON = GsonBuilder().registerTypeAdapter(Pages::class.java, PageAdapter())
                .create()
    }

    private fun createUnsafeOkHttpClient(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.getSocketFactory()

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}

internal
class PageAdapter : JsonDeserializer<Pages> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Pages {
        val pages = arrayListOf<Page>()
        val entrySet = json.asJsonObject
                .entrySet()
        for ((key, value) in entrySet) {
            if (!TextUtils.equals(key, "-1")) {
                val page = BackendModule.GSON.fromJson(value, Page::class.java)
                pages.add(page)
            }
        }
        return Pages(pages)
    }
}