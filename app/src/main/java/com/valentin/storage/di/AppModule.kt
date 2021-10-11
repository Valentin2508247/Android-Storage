package com.valentin.storage.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.valentin.storage.models.AppDatabase
import com.valentin.storage.repository.CatRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Module
object AppModule {

    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        val db = AppDatabase.getDatabase(context)
        GlobalScope.launch(Dispatchers.IO) {
            db.catDao().query("", "")
        }
        return db
    }

//    @Provides
//    fun provideDBHelper(database: AppDatabase): SupportSQLiteDatabase {
//        return try {
//            database.openHelper.writableDatabase
//        }
//        catch (ex: Exception) {
//            database.openHelper.readableDatabase
//        }
//    }


    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
//    @Singleton
    fun provideCatRepository(database: AppDatabase): CatRepository {
        return CatRepository(database)
    }
}