package br.com.jwar.triviachallenge.data.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM ActivityEntity WHERE id = :id")
    fun getById(id: String): Flow<ActivityEntity>
    @Query("SELECT * FROM ActivityEntity WHERE unitId = :unitId")
    fun getByUnitId(unitId: String): Flow<List<ActivityEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)
    @Update
    suspend fun updateActivity(activity: ActivityEntity)
}