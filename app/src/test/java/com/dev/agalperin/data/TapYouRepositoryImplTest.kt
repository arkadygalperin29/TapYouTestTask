package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.TapYouRepositoryTest
import com.dev.agalperin.domain.model.Point
import com.dev.core.AppDispatchers
import com.dev.tapyouapi.TapYouApi
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import io.mockk.every
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TapYouRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcher = AppDispatchersTest(default = testDispatcher, io = testDispatcher, unconfined = testDispatcher)

    private lateinit var mockRepositoryImpl: FakeTapYouRepositoryImpl

    @Before
    fun setUp() {
        mockRepositoryImpl = FakeTapYouRepositoryImpl()
    }

    @Test
    fun `test getPointsFromApi returns correct data`() = runTest {
        // Arrange
//        val count = 10
//        val mockResponse = Point(1.0f, 2.0f)
//            pointsList = listOf(
////                PointDto(x = 5.00, y = 6.00),
////                PointDto(x = 7.00, y = 9.00),
////                PointDto(x = 9.00, y = 11.00),
////                PointDto(x = 11.00, y = 4.00),
////                PointDto(x = 13.00, y = 6.00),
////                PointDto(x = 15.00, y = 28.00),
////                PointDto(x = 17.00, y = 24.00),
////                PointDto(x = 19.00, y = 6.00),
////                PointDto(x = 28.00, y = 14.00),
////                PointDto(x = 32.00, y = 16.00),
//            )
//        )

        // Act
//        `when`(repository.getPointsFromApi(count)).thenReturn(flowOf(mockResponse))
//
//        // Assert
//        val response = repository.getPointsFromApi(count).toList().first()

//        assertEquals(mockResponse, response)
    }
}