package br.com.jwar.learntrail.presentation.screens.home

import br.com.jwar.learntrail.domain.model.Activity
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.domain.model.Unit
import br.com.jwar.learntrail.domain.repositories.ActivityRepository
import br.com.jwar.learntrail.domain.repositories.UnitRepository
import br.com.jwar.learntrail.domain.repositories.UserRepository
import br.com.jwar.learntrail.utils.DataFactory.makeActivitiesList
import br.com.jwar.learntrail.utils.DataFactory.makeActivity
import br.com.jwar.learntrail.utils.DataFactory.makeQuestion
import br.com.jwar.learntrail.utils.DataFactory.makeQuestionsList
import br.com.jwar.learntrail.utils.DataFactory.makeUnit
import br.com.jwar.learntrail.utils.DataFactory.makeUnitsList
import br.com.jwar.learntrail.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ERROR_FETCHING_UNITS_MESSAGE = "error fetching units"

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val unitRepository: UnitRepository = mockk()
    private val activityRepository: ActivityRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(unitRepository, activityRepository, userRepository)
    }

    @Test
    fun `given the units are fetched successfully when load it then map to the ui model and set state`() = runTest {
        val units = makeUnitsList()
        val activities = makeActivitiesList()
        scenario(isLoaded = false, isSucceeded = true, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        val unitModels = viewModel.uiState.value.asLoadedState()?.units.orEmpty()
        assertEquals(units.size, unitModels.size)
        unitModels.forEachIndexed { unitIndex, unitModel ->
            assertEquals(units[unitIndex].id, unitModel.id)
            assertEquals(units[unitIndex].name, unitModel.name)
            assertEquals(units[unitIndex].isUnlocked, unitModel.isUnlocked)
            unitModel.activities.forEachIndexed { activityIndex, activityModel ->
                assertEquals(activities[activityIndex].id, activityModel.id)
                assertEquals(activities[activityIndex].name, activityModel.name)
                assertEquals(activities[activityIndex].isUnlocked, activityModel.isUnlocked)
                assertEquals(activities[activityIndex].isCompleted, activityModel.isCompleted)
            }
        }
    }

    @Test
    fun `given the units are fetched successfully when load it then get and set the user xp on state`() = runTest {
        scenario(isLoaded = false, isSucceeded = true, userXP = 100)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        assertEquals(100, viewModel.uiState.value.asLoadedState()?.userXP)
    }

    @Test
    fun `given all units are locked when load it then unlock first unit`() = runTest {
        val units = makeUnitsList { makeUnit(isUnlocked = false) }
        val activities = makeActivitiesList { makeActivity(isUnlocked = false) }
        scenario(isLoaded = false, isSucceeded = true, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            unitRepository.unlockUnit(units[0].id)
            activityRepository.unlockActivity(activities[0].id)
        }
    }

    @Test
    fun `given all activities of the last unlocked unit are completed when load it then unlock next unit`() = runTest {
        val units = makeUnitsList(3) { index -> makeUnit(isUnlocked = index == 0) }
        val activities = makeActivitiesList(3) { makeActivity(isCompleted = true) }
        scenario(isLoaded = false, isSucceeded = true, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            unitRepository.unlockUnit(units[1].id)
            activityRepository.unlockActivity(activities[0].id)
        }
    }

    @Test
    fun `given the last unlocked activity is completed when load it then unlock next activity`() = runTest {
        val units = makeUnitsList(3) { makeUnit(isUnlocked = true) }
        val activities = makeActivitiesList(3) { index -> makeActivity(isUnlocked = index == 0, isCompleted = index == 0) }
        scenario(isLoaded = false, isSucceeded = true, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            activityRepository.unlockActivity(activities[1].id)
        }
    }

    @Test
    fun `given the units are not fetched successfully when load it then set error state`() = runTest {
        scenario(isLoaded = false, isSucceeded = false)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        assertEquals(ERROR_FETCHING_UNITS_MESSAGE, viewModel.uiState.value.asErrorState()?.error?.message)
    }

    @Test
    fun `given the units are already loaded when refresh then fetch units again`() = runTest {
        scenario(isLoaded = true, isSucceeded = true)

        viewModel.onIntent(HomeViewIntent.Refresh); advanceUntilIdle()

        coVerify { unitRepository.getUnits(true) }
    }

    private fun TestScope.scenario(
        isLoaded: Boolean = true,
        isSucceeded: Boolean = true,
        units: List<Unit> = makeUnitsList(3),
        activities: List<Activity> = units.flatMap { unit -> makeActivitiesList(3) { makeActivity(unitId = unit.id) } },
        questions: List<Question> = activities.flatMap { activity -> makeQuestionsList(3) { makeQuestion(activityId = activity.id) } },
        userXP: Int = 0,
    ) {
        coEvery { unitRepository.getUnits(any()) } returns flow {
            if (isSucceeded) emit(units) else throw IOException(ERROR_FETCHING_UNITS_MESSAGE)
        }
        coEvery { unitRepository.unlockUnit(any()) } just runs
        coEvery { userRepository.getXP() } returns flowOf(userXP)
        coEvery { activityRepository.getActivities(any(), any()) } returns flowOf(activities)
        coEvery { activityRepository.getQuestions(any()) } returns flowOf(questions)

        if (isLoaded) {
            viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()
        }
    }
}