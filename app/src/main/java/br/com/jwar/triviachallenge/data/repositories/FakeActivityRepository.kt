package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeActivityRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
) : ActivityRepository {

    override fun getActivity(unitId: String, activityId: String) = flow {
        val activity = Activity(
            questions = listOf(
                Question(
                    unit = "1",
                    question = "What is the capital of Brazil?",
                    answers = listOf(
                        "Brasília",
                        "Rio de Janeiro",
                        "São Paulo",
                        "Belo Horizonte"
                    ),
                    correctAnswer = "Brasília",
                    type = "multiple",
                    difficulty = "easy",
                ),
                Question(
                    unit = "1",
                    question = "What is the capital of Argentina?",
                    answers = listOf(
                        "Buenos Aires",
                        "Córdoba",
                        "Rosario",
                        "Mendoza"
                    ),
                    correctAnswer = "Buenos Aires",
                    type = "multiple",
                    difficulty = "easy",
                ),
                Question(
                    unit = "1",
                    question = "What is the capital of Chile?",
                    answers = listOf(
                        "Santiago",
                        "Valparaíso",
                        "Concepción",
                        "La Serena"
                    ),
                    correctAnswer = "Santiago",
                    type = "multiple",
                    difficulty = "easy",
                ),
            )
        )
        emit(activity)
    }.flowOn(dispatcher)
}