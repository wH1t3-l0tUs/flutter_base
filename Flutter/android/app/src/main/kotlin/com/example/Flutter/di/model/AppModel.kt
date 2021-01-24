package io.driverdoc.testapp.di.model

import android.app.Application
import androidx.room.Room
import android.content.Context
import com.google.gson.*
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.remote.InteractCommon
import dagger.Module
import dagger.Provides
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModel {
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "mvvm-database").allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    internal fun provideInteractCommon(): InteractCommon {
        return InteractCommon()
    }

    @Provides
    @Singleton
    internal fun provideSchedule(): Executor {
        return Executors.newFixedThreadPool(4)
    }

    @Provides
    @Singleton
    internal fun provideGson() : Gson {
        var gson = GsonBuilder()
        if (io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME.size > 0 ){
            gson = gson.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
                    for (format in io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME) {
                        try {
                            return SimpleDateFormat(format, Locale.getDefault()).parse(json!!.getAsString())
                        } catch (e: ParseException) {
                        }
                    }
                    throw  JsonParseException("Unparseable date: \"" + json!!.getAsString() + "  format: " + io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME.get(0))
                }
            })
        }
        return gson.create()
    }
}