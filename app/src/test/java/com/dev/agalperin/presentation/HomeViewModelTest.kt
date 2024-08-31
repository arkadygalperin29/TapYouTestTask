package com.dev.agalperin.presentation

import android.content.Context
import app.cash.turbine.test
import com.dev.agalperin.data.TapYouRepositoryImpl
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import com.dev.core.AppDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.inject.Provider

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class HomeViewModelTest {


    private lateinit var getAllPointsUsecaseProvider: Provider<GetAllPointsUsecase>

    private lateinit var getAllPointsUsecase: GetAllPointsUsecase

    private lateinit var mockRepository: TapYouRepository

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {

        mockRepository = mock(TapYouRepository::class.java)

        getAllPointsUsecase = GetAllPointsUsecase(mockRepository)

        getAllPointsUsecaseProvider = mock {
            on { get() } doReturn getAllPointsUsecase
        }

        Dispatchers.setMain(testDispatcher)

        val appDispatchers = AppDispatchers(
            default = testDispatcher,
            io = testDispatcher,
            main = testDispatcher,
            unconfined = testDispatcher
        )

        viewModel = spy(
            HomeViewModel(
                getAllPointsUsecase = getAllPointsUsecaseProvider,
                dispatchers = appDispatchers,
                context = mock(Context::class.java)
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `homeViewModel Initialization State Loaded`() = runTest {
        val initialState = viewModel.state.value

        assertEquals(emptyList<Point>(), initialState.points)
        assertFalse(initialState.isLoading)
    }

    @Test
    fun `homeViewModel getAllPoints SuccessScenario`() = runTest {
        val points = listOf(Point(1.0f, 2.0f), Point(1.5f, 3.0f))
        whenever(getAllPointsUsecase.execute(2)).thenReturn(Result.success(points))

        viewModel.getAllPoints(2)

        advanceUntilIdle()

        assertEquals(HomeScreenState(points = points, isLoading = false), viewModel.state.value)
    }

    @Test
    fun `homeViewModel getAllPoints FailureScenario`() = runTest {
        val exception = RuntimeException("Failed to load points")
        whenever(getAllPointsUsecase.execute(2)).thenReturn(Result.failure(exception))

        viewModel.getAllPoints(2)

        advanceUntilIdle()

        assertEquals(HomeScreenState(points = emptyList(), isLoading = false), viewModel.state.value)
    }
}