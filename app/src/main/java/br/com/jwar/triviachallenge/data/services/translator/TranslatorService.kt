package br.com.jwar.triviachallenge.data.services.translator

import com.google.mlkit.nl.translate.Translator

interface TranslatorService {
    suspend fun translate(text: String): String
    suspend fun setLanguage(language: String): Translator
    fun getLanguage(): String
    fun getSupportedLanguages(): Collection<String>
}