package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.LocalLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalLocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocalLocations(localLocation: List<LocalLocation>)

    @Query("SELECT * FROM local_location")
    fun findAllLocalLocations(): Flow<List<LocalLocation>>
}