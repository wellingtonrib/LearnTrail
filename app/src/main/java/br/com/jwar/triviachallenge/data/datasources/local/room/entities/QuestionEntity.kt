package br.com.jwar.triviachallenge.data.datasources.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionEntity(
    @PrimaryKey val id: String,
    val activityId: String,
    val unit: String,
    val correctAnswer: String,
    val difficulty: String,
    val answers: List<String>,
    val question: String,
    val type: String
)