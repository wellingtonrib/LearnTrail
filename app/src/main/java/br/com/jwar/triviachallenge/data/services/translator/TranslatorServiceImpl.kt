package br.com.jwar.triviachallenge.data.services.translator

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.mlkit.common.MlKitException
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

    override fun getSupportedLanguages() = supportedLanguages.values

    override suspend fun translate(text: String): String =
        try {
            getTranslator().translate(text).await()
        } catch (e: MlKitException) {
            text
        }

    override suspend fun setLanguage(language: String): Translator {
        val languageKey = supportedLanguages.entries
            .find { it.value == language }?.key ?: supportedLanguages.keys.first()
        val currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val currentModel = TranslateRemoteModel.Builder(currentLanguage).build()

        RemoteModelManager.getInstance().deleteDownloadedModel(currentModel)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageKey))
        return getTranslator()
    }

    override fun getLanguage() =
        supportedLanguages[AppCompatDelegate.getApplicationLocales().toLanguageTags()] ?:
            supportedLanguages.values.first()

    private suspend fun getTranslator() =
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(AppCompatDelegate.getApplicationLocales().toLanguageTags())
            .build()
            .let { options ->
                Translation.getClient(options).apply {
                    downloadModelIfNeeded().await()
                }
            }
}