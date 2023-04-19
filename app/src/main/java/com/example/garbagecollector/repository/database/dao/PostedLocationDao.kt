package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.PostedLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface PostedLocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPostedLocations(postedLocation: List<PostedLocation>)

    @Query("SELECT * FROM posted_location ORDER BY createDate DESC")
    fun findAllPostedUserLocations(): Flow<List<PostedLocation>>
}