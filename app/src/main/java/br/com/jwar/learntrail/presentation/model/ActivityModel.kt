package br.com.jwar.learntrail.presentation.model

import br.com.jwar.learntrail.domain.model.Activity

data class ActivityModel(
    val id: String,
    val name: String,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
) {
    companion object {
        fun fromActivity(activity: Activity) = ActivityModel(
            id = activity.id,
            name = activity.name,
            isUnlocked = activity.isUnlocked,
            isCompleted = activity.isCompleted,
        )
    }
}