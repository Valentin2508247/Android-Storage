package com.valentin.storage.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseOpenHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {

    override fun onCreate(db: SQLiteDatabase?) {
//        Log.d(TAG, "DatabaseOpenHelper onCreate")
//        db?.execSQL(createSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        Log.d(TAG, "DatabaseOpenHelper onUpgrade")
//        db?.let {
//            db.execSQL(dropSQL)
//        }
//        onCreate(db)
    }

    private companion object {
        const val TAG = "DatabaseOpenHelper"
        const val DATABASE_NAME = "my_database"
        const val SCHEMA = 3

        const val dropSQL = "drop table if exists Cat"
        const val createSQL = "create table if not exists Cat (id integer primary key autoincrement, age integer, name String, breed String);"
    }
}