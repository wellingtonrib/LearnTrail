package br.com.jwar.triviachallenge.data.services.translator

import androidx.appcompat.app.AppCompatDelegate.getApplicationLocales
import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TranslatorServiceImpl @Inject constructor(): TranslatorService {

    private val supportedLanguages = mapOf(
        "en" to "English",
        "pt" to "Portuguese",
    )

    private var translator: Translator? = null

    override fun getSupportedLanguages() = supportedLanguages.values

    override suspend fun translate(text: String): String =
        try {
            getTranslator().translate(text).await()
        } catch (e: Exception) {
            text
        }

    override suspend fun setTargetLanguage(language: String) =
        try {
            deleteCurrentModelIfNeeded()
            setApplicationLanguage(language)
            Result.success(getTranslator(true))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getTargetLanguage(): Map.Entry<String, String> {
        val currentLanguage = getApplicationLocales().toLanguageTags()
        return supportedLanguages.entries
            .find { it.key == currentLanguage } ?: supportedLanguages.entries.first()
    }

    private fun deleteCurrentModelIfNeeded() {
        val currentLanguage = getApplicationLocales().toLanguageTags()
        val currentModel = TranslateRemoteModel.Builder(currentLanguage).build()
        RemoteModelManager.getInstance().deleteDownloadedModel(currentModel)
    }

    private fun setApplicationLanguage(language: String) {
        val languageKey = supportedLanguages.entries
            .find { it.value == language }?.key ?: supportedLanguages.keys.first()
        setApplicationLocales(LocaleListCompat.forLanguageTags(languageKey))
    }

    private suspend fun getTranslator(rebuild: Boolean = false): Translator {
        if (translator == null || rebuild) {
            val language = getTargetLanguage().key
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(language)
                .build()
            translator = Translation.getClient(options)
            translator?.downloadModelIfNeeded()?.await()
        }
        return translator as Translator
    }
}