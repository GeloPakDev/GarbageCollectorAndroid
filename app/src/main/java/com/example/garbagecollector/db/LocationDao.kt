package com.example.garbagecollector.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.garbagecollector.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun loadAll(): LiveData<List<Location>>

    @Query("SELECT * FROM Location WHERE id = :locationId")
    fun loadLocation(locationId: Long): Location

    @Query("SELECT * FROM Location WHERE id = :locationId")
    fun loadLiveLocation(locationId: Long): LiveData<Location>

    @Insert(onConflict = IGNORE)
    fun insertLocation(location: Location): Long

    @Update(onConflict = REPLACE)
    fun updateLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)
}