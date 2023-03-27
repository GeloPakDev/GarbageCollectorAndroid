package com.example.garbagecollector.db

import android.content.Context
import androidx.room.*
import com.example.garbagecollector.db.model.Location
import com.example.garbagecollector.db.model.User
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.db.model.converters.BitmapConverter
import com.example.garbagecollector.db.model.converters.LocalDateConverter
import com.example.garbagecollector.db.model.converters.RankConverter
import com.example.garbagecollector.db.model.converters.StateConverter

@Database(entities = [Location::class, User::class], version = 9)
@TypeConverters(
    BitmapConverter::class,
    LocalDateConverter::class,
    StateConverter::class,
    RankConverter::class
)
abstract class GarbageCollectorDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun userDao(): UserDao

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