package br.com.jwar.triviachallenge.data.services.translator

interface TranslatorService {
    suspend fun translate(text: String): String
}