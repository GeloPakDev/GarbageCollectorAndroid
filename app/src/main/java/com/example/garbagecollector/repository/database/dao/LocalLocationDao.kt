package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.LocalLocation

@Dao
interface LocalLocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocalLocations(localLocation: List<LocalLocation>)

    @Query("SELECT * FROM local_location")
    fun findAllLocalLocations(): List<LocalLocation>

    @Query("DELETE FROM local_location WHERE id = :id")
    fun deleteLocalLocationById(id: Long)
}