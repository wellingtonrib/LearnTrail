package br.com.jwar.learntrail.data.datasources.local.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isUnlocked: Boolean,
)