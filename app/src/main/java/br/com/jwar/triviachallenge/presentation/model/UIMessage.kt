package br.com.jwar.triviachallenge.presentation.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class UIMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: UIText,
    val style: UIMessageStyle = UIMessageStyle.SUCCESS
) {
    fun getColor() = when (style) {
        UIMessageStyle.SUCCESS -> Color.Green
        UIMessageStyle.WARNING -> Color.Yellow
        UIMessageStyle.DANGER -> Color.Red
    }
}

enum class UIMessageStyle {
    SUCCESS, WARNING, DANGER
}