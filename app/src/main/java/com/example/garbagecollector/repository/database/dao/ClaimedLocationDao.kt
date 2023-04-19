package com.example.garbagecollector.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface ClaimedLocationDao {
    @Query("SELECT * FROM claimed_location ORDER BY claimDate DESC")
    fun findAllClaimedUserLocations(): Flow<List<ClaimedLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertClaimedLocations(claimedLocations: List<ClaimedLocation>)
}