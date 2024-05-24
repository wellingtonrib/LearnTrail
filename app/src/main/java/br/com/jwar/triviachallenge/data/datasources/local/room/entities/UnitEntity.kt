package br.com.jwar.triviachallenge.data.datasources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnitEntity(
    @PrimaryKey val id: String,
    val name: String,
)