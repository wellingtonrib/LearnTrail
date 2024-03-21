package br.com.jwar.triviachallenge.data.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.LessonEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM UnitEntity")
    fun getUnits(): Flow<List<UnitEntity>>

    @Query("SELECT * FROM LessonEntity WHERE unitId = :unitId")
    fun getLessons(unitId: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<UnitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)
}