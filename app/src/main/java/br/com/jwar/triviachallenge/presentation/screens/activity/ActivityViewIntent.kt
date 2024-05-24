package br.com.jwar.triviachallenge.presentation.screens.activity

import br.com.jwar.triviachallenge.presentation.utils.UIMessage

sealed class ActivityViewIntent {
    data class LoadActivity(val activityId: String): ActivityViewIntent()
    data class SelectAnswer(val answer: String): ActivityViewIntent()
    object CheckAnswer: ActivityViewIntent()
    object Next: ActivityViewIntent()
    object Finish: ActivityViewIntent()
    data class MessageShown(val uiMessage: UIMessage): ActivityViewIntent()
}