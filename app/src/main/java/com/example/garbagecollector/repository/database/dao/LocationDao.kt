package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.Location


@Dao
interface LocationDao {

    @Query("SELECT * FROM location WHERE id = :id")
    fun findLocationById(id: Long): Location

    @Insert(onConflict = IGNORE)
    fun create(location: Location): Long

    @Query("DELETE FROM location WHERE id = :id")
    fun deleteLocationById(id: Long)
}
