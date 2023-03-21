package com.example.garbagecollector.db

import android.content.Context
import androidx.room.*
import com.example.garbagecollector.model.Location
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.Converters

@Database(entities = [Location::class], version = 5)
@TypeConverters(Converters::class)
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
                ).fallbackToDestructiveMigration().build()
            }
            return instance as GarbageCollectorDatabase
        }
    }
}