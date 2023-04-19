package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location WHERE state = 'NEW'")
    fun findAllNewLocations(): Flow<List<Location>>

    @Insert(onConflict = IGNORE)
    fun create(location: Location): Long

    @Insert(onConflict = IGNORE)
    fun insertLocations(locations: List<Location>)


    @Query("UPDATE location SET state = 'CLAIMED' WHERE id = :locationId")
    fun updateLocationState(locationId: Long)
}
