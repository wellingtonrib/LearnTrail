package br.com.jwar.learntrail.presentation.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.UUID

data class UIMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: UIText,
    val style: UIMessageStyle = UIMessageStyle.SUCCESS
) {
    @Composable
    fun getColor() = when (style) {
        UIMessageStyle.SUCCESS -> MaterialTheme.colorScheme.primary
        UIMessageStyle.WARNING -> MaterialTheme.colorScheme.tertiary
        UIMessageStyle.DANGER -> MaterialTheme.colorScheme.error
    }
}

enum class UIMessageStyle {
    SUCCESS, WARNING, DANGER
}