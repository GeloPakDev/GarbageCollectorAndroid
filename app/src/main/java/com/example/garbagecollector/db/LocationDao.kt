package com.example.garbagecollector.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.garbagecollector.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun findAll(): LiveData<List<Location>>

    @Query("SELECT * FROM Location WHERE id = :locationId")
    fun findLocationById(locationId: Long): Location

    @Query("SELECT * FROM Location WHERE id = :locationId")
    fun loadLiveLocation(locationId: Long): LiveData<Location>

    @Insert(onConflict = IGNORE)
    fun create(location: Location): Long

    @Update(onConflict = REPLACE)
    fun update(location: Location)

    @Delete
    fun delete(location: Location)
}