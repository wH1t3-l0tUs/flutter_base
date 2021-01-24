package io.driverdoc.testapp.data.remote

import com.google.gson.*
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.driverdoc.testapp.BuildConfig
import io.driverdoc.testapp.common.Constants
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ApiHelp {
    private fun createOkHttp(timeOut: Int = 4, headers: MutableList<Pair<String, String>> = mutableListOf()): OkHttpClient {
        val client = OkHttpClient.Builder()
        val logging = LoggingInterceptor.Builder()
                .loggable(io.driverdoc.testapp.common.Constants.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .addHeader("appVersion", io.driverdoc.testapp.BuildConfig.VERSION_NAME)
                .addHeader("platform", Constants.OS_TYPE)
        if (headers.isNotEmpty()) {
            for (s in headers) {
                logging.addHeader(s.first, s.second)
            }
        }
        client.addInterceptor(logging.build())
        return client
                .retryOnConnectionFailure(true)
                .connectTimeout(timeOut.toLong(), TimeUnit.MINUTES)
                .readTimeout(timeOut.toLong(), TimeUnit.MINUTES)
                .writeTimeout(timeOut.toLong(), TimeUnit.MINUTES)
                .build()
    }

    fun createRetrofit(timeOut: Int = 4, endpoint: String, formatDate: List<String>, headers: MutableList<Pair<String, String>> = mutableListOf()): Retrofit {
        var gson = GsonBuilder()
        if (formatDate.size > 0) {
            gson = gson.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
                    for (format in formatDate) {
                        try {
                            return SimpleDateFormat(format, Locale.getDefault()).parse(json!!.getAsString())
                        } catch (e: ParseException) {
                        }
                    }
                    throw  JsonParseException("Unparseable date: \"" + json!!.getAsString() + "  format: " + formatDate.get(0))
                }
            })
        }

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttp(timeOut, headers))
                .build()
    }

    fun createRetrofitMapApi(timeOut: Int = 4, endpoint: String, formatDate: List<String>, headers: MutableList<Pair<String, String>> = mutableListOf()): Retrofit {
        var gson = GsonBuilder()
        if (formatDate.size > 0) {
            gson = gson.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
                    for (format in formatDate) {
                        try {
                            return SimpleDateFormat(format, Locale.getDefault()).parse(json!!.getAsString())
                        } catch (e: ParseException) {
                        }
                    }
                    throw  JsonParseException("Unparseable date: \"" + json!!.getAsString() + "  format: " + formatDate.get(0))
                }
            })
        }

        return Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttp(timeOut, headers))
                .build()
    }

}