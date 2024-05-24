package br.com.jwar.triviachallenge.data.datasources.local.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.ActivityDao
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.QuestionDao
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.UnitDao
import br.com.jwar.triviachallenge.data.datasources.local.database.room.entities.ActivityEntity
import br.com.jwar.triviachallenge.data.datasources.local.database.room.entities.QuestionEntity
import br.com.jwar.triviachallenge.data.datasources.local.database.room.entities.UnitEntity

const val APP_DATABASE_NAME = "database"

@Database(
    entities = [
        UnitEntity::class,
        ActivityEntity::class,
        QuestionEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun unitDao(): UnitDao
    abstract fun activityDao(): ActivityDao
    abstract fun questionDao(): QuestionDao
}