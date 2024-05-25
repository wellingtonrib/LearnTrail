package br.com.jwar.learntrail.data.datasources.local.database.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["unitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActivityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val unitId: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
)