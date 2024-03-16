package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeUnitRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
) : UnitRepository {
    override fun getUnits() = flow {
        val units = listOf(
            Unit(
                id = "1",
                name = "Unit 1",
                lessons = listOf(
                    Lesson(id = "1", name = "Lesson 1"),
                    Lesson(id = "2", name = "Lesson 2"),
                    Lesson(id = "3", name = "Lesson 3")
                )
            ),
            Unit(
                id = "2",
                name = "Unit 2",
                lessons = listOf(
                    Lesson(id = "1", name = "Lesson 1"),
                    Lesson(id = "2", name = "Lesson 2"),
                    Lesson(id = "3", name = "Lesson 3")
                )
            ),
            Unit(
                id = "3",
                name = "Unit 3",
                lessons = listOf(
                    Lesson(id = "1", name = "Lesson 1"),
                    Lesson(id = "2", name = "Lesson 2"),
                    Lesson(id = "3", name = "Lesson 3")
                )
            )
        )
        emit(units)
    }.flowOn(dispatcher)
}