package com.valentin.storage.di

import android.content.Context
import com.valentin.storage.activity.MainActivity
import com.valentin.storage.fragment.AddFragment
import com.valentin.storage.fragment.CatsFragment
import com.valentin.storage.fragment.UpdateFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    //activity
    fun inject(activity: MainActivity)

    //fragments
    fun inject(fragment: CatsFragment)
    fun inject(fragment: AddFragment)
    fun inject(fragment: UpdateFragment)
}