package com.example.garbagecollector.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.garbagecollector.model.Location
import com.example.garbagecollector.util.Constants

@Database(entities = [Location::class], version = 1)
abstract class GarbageCollectorDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        private var instance: GarbageCollectorDatabase? = null
        fun getInstance(context: Context): GarbageCollectorDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GarbageCollectorDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
            }
            return instance as GarbageCollectorDatabase
        }
    }
}