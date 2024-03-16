package br.com.jwar.triviachallenge.data.datasources.trivia

import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaQuestionsResponse

interface TriviaRemoteDataSource {
    suspend fun getQuestion(category: String, difficult: String): TriviaQuestionsResponse
    suspend fun getCategories(): List<TriviaCategoryResponse>
}