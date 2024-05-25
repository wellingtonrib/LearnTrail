package br.com.jwar.learntrail.data.datasources.local.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.jwar.learntrail.data.datasources.local.database.room.entities.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM UnitEntity WHERE id = :unitId")
    fun findById(unitId: String): Flow<UnitEntity>

    @Query("SELECT * FROM UnitEntity")
    fun getAll(): Flow<List<UnitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: UnitEntity)

    @Update
    suspend fun update(unit: UnitEntity)
}