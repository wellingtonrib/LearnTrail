package br.com.jwar.learntrail.presentation.screens.settings

import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.data.services.translator.Language
import br.com.jwar.learntrail.data.services.translator.TranslatorService
import br.com.jwar.learntrail.presentation.utils.UIText
import br.com.jwar.learntrail.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val translatorService: TranslatorService = mockk {
        coEvery { getTargetLanguage() } returns Language.EN
    }
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(translatorService)
    }

    @Test
    fun `given the language is set successfully when select language then update state with the selected language and message`() = runTest {
        coEvery { translatorService.setTargetLanguage(Language.PT) } returns Result.success(mockk())

        viewModel.onIntent(SettingsViewIntent.SelectLanguage(Language.PT)); advanceUntilIdle()
        viewModel.uiState.value.userMessages.forEach { println(it.text) }

        assertEquals(Language.PT, viewModel.uiState.value.currentLanguage)
        assertEquals(R.string.message_language_changed, (viewModel.uiState.value.userMessages[0].text as UIText.StringResource).resId)
    }

    @Test
    fun `given the language is failed to set when select language then update state with the current language and message`() = runTest {
        coEvery { translatorService.setTargetLanguage(Language.PT) } returns Result.failure(Exception("error"))

        viewModel.onIntent(SettingsViewIntent.SelectLanguage(Language.PT)); advanceUntilIdle()

        assertEquals(Language.EN, viewModel.uiState.value.currentLanguage)
        assertEquals("error", (viewModel.uiState.value.userMessages[0].text as UIText.DynamicString).value)
    }

}
