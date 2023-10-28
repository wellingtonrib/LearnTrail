package br.com.jwar.triviachallenge.data.services.translator

import com.google.mlkit.nl.translate.Translator

interface TranslatorService {
    suspend fun translate(text: String): String
    suspend fun setTargetLanguage(language: String): Result<Translator>
    fun getTargetLanguage(): Map.Entry<String, String>
    fun getSupportedLanguages(): Collection<String>
}