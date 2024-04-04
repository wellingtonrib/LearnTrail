package br.com.jwar.triviachallenge.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isUnlocked: Boolean,
)