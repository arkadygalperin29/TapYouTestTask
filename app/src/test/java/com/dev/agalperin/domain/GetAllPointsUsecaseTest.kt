package com.dev.agalperin.domain

import com.dev.agalperin.data.FakeTapYouRepositoryImpl
import com.dev.agalperin.domain.model.Point
import com.dev.agalperin.domain.model.PointTest
import com.dev.agalperin.domain.model.PointsResponseTest
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllPointsUsecaseTest {

    private lateinit var fakeRepository: FakeTapYouRepositoryImpl
    private lateinit var fakeGetAllPointsUsecase: FakeGetAllPointsUsecase

    @Before
    fun setUp() {
        fakeRepository = FakeTapYouRepositoryImpl()
        fakeGetAllPointsUsecase = FakeGetAllPointsUsecase(fakeRepository)
    }

    @Test
    fun `test execute returns points sorted by x coordinate`() = runTest {

        val actualPoints = fakeGetAllPointsUsecase.execute(5)

        val expectedPoints = listOf(
            PointTest(x = 5.0, y = 5.0),
            PointTest(x = 5.0, y = 7.0),
            PointTest(x = 8.0, y = 4.0),
            PointTest(x = 10.0, y = 7.0),
            PointTest(x = 13.0, y = 11.0)
        )

        assertEquals(expectedPoints, actualPoints)
    }

    @Test
    fun `test execute returns empty list when repository returns empty`() = runTest {

        val emptyFakeRepository = object : TapYouRepositoryTest {
            override suspend fun getPointsFromApi(count: Int): PointsResponseTest<PointTest> {
                return PointsResponseTest(emptyList())
            }
        }
        val emptyFakeUsecase = FakeGetAllPointsUsecase(emptyFakeRepository)


        val actualPoints = emptyFakeUsecase.execute(0)


        assertEquals(emptyList<PointTest>(), actualPoints)
    }

    @Test
    fun `test execute returns correctly sorted points when multiple points have the same x value`() = runTest {
        val pointsWithSameX = listOf(
            PointTest(x = 5.0, y = 8.0),
            PointTest(x = 5.0, y = 7.0),
            PointTest(x = 5.0, y = 5.0),
            PointTest(x = 8.0, y = 4.0),
            PointTest(x = 13.0, y = 11.0)
        )

        val fakeRepository = object : TapYouRepositoryTest {
            override suspend fun getPointsFromApi(count: Int): PointsResponseTest<PointTest> {
                return PointsResponseTest(pointsWithSameX)
            }
        }
        val fakeUsecase = FakeGetAllPointsUsecase(fakeRepository)

        val actualPoints = fakeUsecase.execute(5)

        val expectedPoints = pointsWithSameX.sortedBy { it.x }
        assertEquals(expectedPoints, actualPoints)
    }
}