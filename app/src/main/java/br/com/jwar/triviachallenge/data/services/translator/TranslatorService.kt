package br.com.jwar.triviachallenge.data.services.translator

import com.google.mlkit.nl.translate.Translator

interface TranslatorService {
    suspend fun translate(text: String): String
    suspend fun setTargetLanguage(language: Language): Result<Translator>
    fun getTargetLanguage(): Language
}