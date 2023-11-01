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
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class TranslatorServiceImpl @Inject constructor(): TranslatorService {

    private var translator: Translator? = null

    override suspend fun translate(text: String): String =
        try {
            getTranslator().translate(text).await()
        } catch (e: Exception) {
            text
        }

    override suspend fun setTargetLanguage(language: Language) =
        try {
            deleteCurrentModelIfNeeded()
            setApplicationLocales(LocaleListCompat.forLanguageTags(language.name.lowercase()))
            Result.success(getTranslator(true))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getTargetLanguage(): Language {
        val currentLanguage = getApplicationLocales().toLanguageTags()
        return Language.values().find { it.name.lowercase() == currentLanguage } ?: Language.EN
    }

    private fun deleteCurrentModelIfNeeded() {
        val currentLanguage = getApplicationLocales().toLanguageTags()
        val currentModel = TranslateRemoteModel.Builder(currentLanguage).build()
        RemoteModelManager.getInstance().deleteDownloadedModel(currentModel)
    }

    private suspend fun getTranslator(rebuild: Boolean = false): Translator {
        if (translator == null || rebuild) {
            val language = getTargetLanguage().name.lowercase()
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