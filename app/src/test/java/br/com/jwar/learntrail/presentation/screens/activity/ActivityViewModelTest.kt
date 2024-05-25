package br.com.jwar.learntrail.presentation.screens.activity

import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.domain.repositories.ActivityRepository
import br.com.jwar.learntrail.domain.repositories.UserRepository
import br.com.jwar.learntrail.presentation.utils.UIMessageStyle
import br.com.jwar.learntrail.utils.DataFactory
import br.com.jwar.learntrail.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val activityRepository: ActivityRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var viewModel: ActivityViewModel

    @Before
    fun setup() {
        viewModel = ActivityViewModel(activityRepository, userRepository)
    }

    @Test
    fun `given the questions are fetched successfully when load activity then set the loaded state`() = runTest {
        val questions = DataFactory.makeQuestionsList()
        scenario(isLoaded = false, questions = questions)

        viewModel.onIntent(ActivityViewIntent.LoadActivity("activityId")); advanceUntilIdle()

        viewModel.uiState.value.asLoaded()?.let { state ->
            assertEquals(questions, state.questions)
            assertEquals(questions.first(), state.currentQuestion)
            assertFalse(state.isResultShown)
            assertNull(state.selectedAnswer)
            assertEquals("1/3", state.progress)
        }
    }

    @Test
    fun `given the questions fetched failure when load activity then set the error state`() = runTest {
        scenario(isLoaded = false, isSucceeded = false)

        viewModel.onIntent(ActivityViewIntent.LoadActivity("activityId")); advanceUntilIdle()

        assert(viewModel.uiState.value is ActivityViewState.Error)
    }

    @Test
    fun `given the questions is empty when load activity then set the error state`() = runTest {
        scenario(isLoaded = false, questions = emptyList())

        viewModel.onIntent(ActivityViewIntent.LoadActivity("activityId")); advanceUntilIdle()

        assert(viewModel.uiState.value is ActivityViewState.Error)
    }

    @Test
    fun `given the activity is loaded when select answer then set state selected answer`() = runTest {
        scenario(isLoaded = true)

        viewModel.onIntent(ActivityViewIntent.SelectAnswer("correctAnswer"))

        viewModel.uiState.value.asLoaded()?.let { state ->
            assertFalse(state.isResultShown)
            assertEquals("correctAnswer", state.selectedAnswer)
        }
    }

    @Test
    fun `given the selectedAnswer is correct when check answer then increment state points and add a success message`() = runTest {
        scenario(isLoaded = true, selectedAnswer = "correctAnswer")

        viewModel.onIntent(ActivityViewIntent.CheckAnswer)

        viewModel.uiState.value.asLoaded()?.let { state ->
            assert(state.isResultShown)
            assertEquals(1, state.points)
            assert(state.userMessages.any { it.style == UIMessageStyle.SUCCESS })
        }
    }

    @Test
    fun `given the selectedAnswer is wrong when check answer then decremented state attemptsLeft and add a danger message`() = runTest {
        scenario(isLoaded = true, selectedAnswer = "wrongAnswer")

        viewModel.onIntent(ActivityViewIntent.CheckAnswer)

        viewModel.uiState.value.asLoaded()?.let { state ->
            assert(state.isResultShown)
            assertEquals(2, state.attemptsLeft)
            assert(state.userMessages.any { it.style == UIMessageStyle.DANGER })
        }
    }

    @Test
    fun `given has next question when next intent then update state progress, clear selected answer and change the current question`() = runTest {
        val questions = DataFactory.makeQuestionsList(3)
        scenario(isLoaded = true, questions = questions)

        viewModel.onIntent(ActivityViewIntent.Next)

        viewModel.uiState.value.asLoaded()?.let { state ->
            assertFalse(state.isResultShown)
            assertNull(state.selectedAnswer)
            assertEquals(questions[1], state.currentQuestion)
            assertEquals("2/3", state.progress)
        }
    }

    @Test
    fun `given has no next question when next intent then set the finish state with success, complete activity and add XP`() = runTest {
        scenario(isLoaded = true, questions = listOf(DataFactory.makeQuestion()), selectedAnswer = "correctAnswer", isAnswerChecked = true)

        viewModel.onIntent(ActivityViewIntent.Next)

        viewModel.uiState.value.asLoaded()?.let { state ->
            assert(state.isFinished)
            assert(state.isSucceeded)
        }
        coVerify { activityRepository.completeActivity("activityId") }
        coVerify { userRepository.addXP(1) }
    }

    @Test
    fun `given has no attempts remaining when next intent then set finish state with failure`() = runTest {
        scenario(isLoaded = true, hasAttemptsRemaining = false)

        viewModel.onIntent(ActivityViewIntent.Next)

        viewModel.uiState.value.asLoaded()?.let { state ->
            assert(state.isFinished)
            assertFalse(state.isSucceeded)
        }
    }

    @Test
    fun `when finish intent then send navigate to home effect`() = runTest {
        scenario(isLoaded = true)

        viewModel.onIntent(ActivityViewIntent.Finish)

        assert(viewModel.uiEffect.first() is ActivityViewEffect.NavigateToHome)
    }

    @Test
    fun `given a message is shown when message shown intent then remove the message from the state`() = runTest {
        scenario(isLoaded = true, selectedAnswer = "wrongAnswer", isAnswerChecked = true)

        val message = viewModel.uiState.value.asLoaded()?.userMessages?.first()!!
        viewModel.onIntent(ActivityViewIntent.MessageShown(message))

        assertFalse(viewModel.uiState.value.asLoaded()?.userMessages?.contains(message) ?: true)
    }

    private fun TestScope.scenario(
        isLoaded: Boolean = true,
        isSucceeded: Boolean = true,
        selectedAnswer: String? = null,
        isAnswerChecked: Boolean = false,
        hasAttemptsRemaining: Boolean = true,
        questions: List<Question> = DataFactory.makeQuestionsList(),
    ) {
        coEvery { activityRepository.getQuestions(any()) } returns flow {
            if (isSucceeded) emit(questions) else throw Exception()
        }
        coEvery { activityRepository.completeActivity(any()) } just runs
        coEvery { userRepository.addXP(any()) } just runs

        if (isLoaded) {
            viewModel.onIntent(ActivityViewIntent.LoadActivity("activityId")); advanceUntilIdle()
        }

        if (selectedAnswer != null) {
            viewModel.onIntent(ActivityViewIntent.SelectAnswer(selectedAnswer))
        }

        if (isAnswerChecked) {
            viewModel.onIntent(ActivityViewIntent.CheckAnswer)
        }

        if (!hasAttemptsRemaining) {
            repeat(3) {
                viewModel.onIntent(ActivityViewIntent.SelectAnswer("wrongAnswer"))
                viewModel.onIntent(ActivityViewIntent.CheckAnswer)
                viewModel.onIntent(ActivityViewIntent.Next)
            }
        }
    }
}