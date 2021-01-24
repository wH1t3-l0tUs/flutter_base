package io.driverdoc.testapp.di.compoment

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.di.model.AppModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import java.util.concurrent.Executor
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModel::class])
interface AppComponent {
    fun inject(application: MVVMApplication)


    fun context(): Context
    fun appDatabase(): AppDatabase
    fun interactCommon(): InteractCommon
    fun schedule(): Executor
    fun gson(): Gson

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}