package br.com.jwar.triviachallenge.presentation.screens.home

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.domain.repositories.UserRepository
import br.com.jwar.triviachallenge.presentation.model.ActivityModel
import br.com.jwar.triviachallenge.presentation.model.UnitModel
import br.com.jwar.triviachallenge.utils.FakeFactory
import br.com.jwar.triviachallenge.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
        val units = FakeFactory.makeUnitsList()
        val activities = FakeFactory.makeActivitiesList()
        val unitModels = units.map { unit ->
            UnitModel.fromUnit(
                unit = unit,
                activities = activities.map { activity -> ActivityModel.fromActivity(activity) }
            )
        }
        scenario(isLoaded = false, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        assertEquals(unitModels, viewModel.uiState.value.asLoadedState()?.units)
    }

    @Test
    fun `given the units are fetched successfully when load it then get and set the user xp on state`() = runTest {
        val units = FakeFactory.makeUnitsList()
        val activities = FakeFactory.makeActivitiesList()
        val userXP = 100
        scenario(isLoaded = false, units = units, activities = activities, userXP = userXP)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        assertEquals(userXP, viewModel.uiState.value.asLoadedState()?.userXP)
    }

    @Test
    fun `given all units are locked when load it then unlock first unit`() = runTest {
        val units = FakeFactory.makeUnitsList(3) {
            FakeFactory.makeUnit(isUnlocked = false)
        }
        val activities = FakeFactory.makeActivitiesList()
        scenario(isLoaded = false, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            unitRepository.unlockUnit(units[0].id)
            activityRepository.unlockActivity(activities[0].id)
        }
    }

    @Test
    fun `given all activities of the last unlocked unit are completed when load it then unlock next unit`() = runTest {
        val units = FakeFactory.makeUnitsList(3) { index ->
            FakeFactory.makeUnit(isUnlocked = index == 0)
        }
        val activities = FakeFactory.makeActivitiesList(3) {
            FakeFactory.makeActivity(isCompleted = true)
        }
        scenario(isLoaded = false, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            unitRepository.unlockUnit(units[1].id)
            activityRepository.unlockActivity(activities[0].id)
        }
    }

    @Test
    fun `given the last unlocked activity is completed when load it then unlock next activity`() = runTest {
        val units = FakeFactory.makeUnitsList(3) {
            FakeFactory.makeUnit(isUnlocked = true)
        }
        val activities = FakeFactory.makeActivitiesList(3) { index ->
            FakeFactory.makeActivity(isUnlocked = index == 0, isCompleted = index == 0)
        }
        scenario(isLoaded = false, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        coVerify {
            activityRepository.unlockActivity(activities[1].id)
        }
    }

    @Test
    fun `given the units are not fetched successfully when load it then set error state`() = runTest {
        val errorMessage = "error fetching units"
        scenario(isLoaded = false, unitsError = Exception(errorMessage))

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.asErrorState()?.error?.message)
    }

    @Test
    fun `given the units are already loaded when refresh then fetch units again`() = runTest {
        scenario(isLoaded = true)

        viewModel.onIntent(HomeViewIntent.Refresh); advanceUntilIdle()

        coVerify { unitRepository.getUnits(true) }
    }

    private fun TestScope.scenario(
        isLoaded: Boolean,
        units: List<Unit> = emptyList(),
        unitsError: Throwable? = null,
        activities: List<Activity> = emptyList(),
        userXP: Int = 0,
    ) {
        coEvery { unitRepository.getUnits(any()) } returns flow {
            if (unitsError != null) {
                throw unitsError
            } else {
                emit(units)
            }
        }
        coEvery { unitRepository.unlockUnit(any()) } just runs
        coEvery { userRepository.getXP() } returns flowOf(userXP)
        coEvery { activityRepository.getActivities(any(), any()) } returns flowOf(activities)

        if (isLoaded) {
            viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()
        }
    }
}