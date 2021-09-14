package com.valentin.storage.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.valentin.storage.models.AppDatabase
import com.valentin.storage.models.DatabaseOpenHelper
import com.valentin.storage.repository.CatRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideDBHelper(context: Context): DatabaseOpenHelper {
        return DatabaseOpenHelper(context)
    }


    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
//    @Singleton
    fun provideCatRepository(database: AppDatabase, dbHelper: DatabaseOpenHelper): CatRepository {
        return CatRepository(database, dbHelper)
    }
}