package br.com.jwar.triviachallenge.data.services.translator

import com.google.mlkit.common.MlKitException
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class TranslatorServiceImpl @Inject constructor(): TranslatorService {

    private val options by lazy {
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(Locale.getDefault().language)
            .build()
    }

    override suspend fun translate(text: String): String {
        val englishTranslator = Translation.getClient(options).apply {
            downloadModelIfNeeded().await()
        }
        return try {
            englishTranslator.translate(text).await()
        } catch (e: MlKitException) {
            text
        }
    }
}