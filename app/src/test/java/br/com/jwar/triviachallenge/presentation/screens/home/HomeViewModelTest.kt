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
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
    fun `given the units are fetched successfully when load units then set the loaded state`() = runTest {
        val units = FakeFactory.makeUnitsList()
        val activities = FakeFactory.makeActivitiesList()
        scenario(isLoaded = false, units = units, activities = activities)

        viewModel.onIntent(HomeViewIntent.LoadUnits); advanceUntilIdle()

        (viewModel.uiState.value as HomeViewState.Loaded).let { state ->
            val unitModels = units.map { unit ->
                UnitModel.fromUnit(
                    unit = unit,
                    activities = activities.map { activity ->
                        ActivityModel.fromActivity(activity)
                    }
                )
            }
            assertEquals(unitModels, state.units)
        }
    }

    private fun scenario(
        isLoaded: Boolean,
        units: List<Unit> = emptyList(),
        activities: List<Activity> = emptyList(),
    ) {
        coEvery { unitRepository.getUnits(any()) } returns flow { emit(units) }
        coEvery { activityRepository.getActivities(any(), any()) } returns flow { emit(activities) }

        if (isLoaded) {
            viewModel.onIntent(HomeViewIntent.LoadUnits)
        }
    }
}