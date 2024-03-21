package br.com.jwar.triviachallenge.data.datasources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.UnitDao
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.LessonEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity

const val APP_DATABASE_NAME = "database"

@Database(
    entities = [
        UnitEntity::class,
        LessonEntity::class,
    ],
    version = 1
)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun unitDao(): UnitDao
}