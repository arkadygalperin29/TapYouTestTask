package com.dev.agalperin.domain

import com.dev.agalperin.data.TapYouRepositoryImpl
import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.TapYouApi
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy

@RunWith(MockitoJUnitRunner::class)
class GetAllPointsUsecaseTest {

    private lateinit var getPointsUsecase: GetAllPointsUsecase
    private val fakeRepository = mock(TapYouRepository::class.java)

    @Before
    fun setup() {
        getPointsUsecase = GetAllPointsUsecase(fakeRepository)
    }

    @Test
    fun `test execute returns correct points`() = runTest {

        val count = 5
        val mockPointDtoList = listOf(
            PointDto(x = 1.0, y = 2.0),
            PointDto(x = 3.0, y = 4.0),
            PointDto(x = 5.0, y = 6.0),
            PointDto(x = 6.0, y = 4.0),
            PointDto(x = 8.0, y = 4.0)
        )
        val mockPointsResponse = PointsResponse(pointsList = mockPointDtoList)

        `when`(fakeRepository.getPointsFromApi(count)).thenReturn(flowOf(mockPointsResponse))

        val resultFlow = getPointsUsecase.execute(count)

        resultFlow.collect { result ->
            assertEquals(5, result.size)
            assertTrue(result.contains(Point(x = 1.0.toFloat(), y = 2.0.toFloat())))
            assertTrue(result.contains(Point(x = 3.0.toFloat(), y = 4.0.toFloat())))
            assertTrue(result.contains(Point(x = 5.0.toFloat(), y = 6.0.toFloat())))
            assertTrue(result.contains(Point(x = 6.0.toFloat(), y = 4.0.toFloat())))
            assertTrue(result.contains(Point(x = 8.0.toFloat(), y = 4.0.toFloat())))
        }
    }
}