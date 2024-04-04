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
    fun findById(id: String): Flow<ActivityEntity>

    @Query("SELECT * FROM ActivityEntity WHERE unitId = :unitId")
    fun findByUnitId(unitId: String): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity)

    @Update
    suspend fun update(activity: ActivityEntity)

    @Query("UPDATE ActivityEntity SET name = :name, unitId = :unitId WHERE id = :id")
    suspend fun update(id: String, name: String, unitId: String)
}