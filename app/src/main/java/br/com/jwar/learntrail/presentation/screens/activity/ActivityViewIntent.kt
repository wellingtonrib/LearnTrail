package br.com.jwar.learntrail.presentation.screens.activity

import br.com.jwar.learntrail.presentation.utils.UIMessage

sealed class ActivityViewIntent {
    data class LoadActivity(val activityId: String): ActivityViewIntent()
    data class SelectAnswer(val answer: String): ActivityViewIntent()
    object CheckAnswer: ActivityViewIntent()
    object Next: ActivityViewIntent()
    object Finish: ActivityViewIntent()
    data class MessageShown(val uiMessage: UIMessage): ActivityViewIntent()
}