package com.example.garbagecollector.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.example.garbagecollector.db.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location WHERE state = 'NEW'")
    fun findAllNewLocations(): LiveData<List<Location>>

    @Insert(onConflict = IGNORE)
    fun create(location: Location): Long

    @Query("UPDATE Location SET state = 'CLAIMED' WHERE id = :locationId")
    fun updateLocationState(locationId: Long)
}