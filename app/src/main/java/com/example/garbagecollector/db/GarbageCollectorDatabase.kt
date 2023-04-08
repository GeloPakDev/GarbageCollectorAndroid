package com.example.garbagecollector.db

import android.content.Context
import androidx.room.*
import com.example.garbagecollector.db.model.Location
import com.example.garbagecollector.db.model.User
import com.example.garbagecollector.db.model.converters.*
import com.example.garbagecollector.util.Constants

@Database(entities = [Location::class, User::class], version = 13)
@TypeConverters(
    BitmapConverter::class,
    LocalDateConverter::class,
    StateConverter::class,
    RankConverter::class,
    RoleConverter::class
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