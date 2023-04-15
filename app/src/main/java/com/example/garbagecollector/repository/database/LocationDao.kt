package com.example.garbagecollector.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location WHERE state = 'NEW'")
    fun findAllNewLocations(): Flow<List<Location>>

    @Insert(onConflict = IGNORE)
    fun create(location: Location): Long

    @Insert(onConflict = IGNORE)
    fun insertLocations(location: List<Location>)

    @Query("UPDATE Location SET state = 'CLAIMED' WHERE id = :locationId")
    fun updateLocationState(locationId: Long)
}
