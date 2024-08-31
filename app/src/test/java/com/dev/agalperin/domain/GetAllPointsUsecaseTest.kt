package com.dev.agalperin.domain

import org.junit.Before

import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class GetAllPointsUsecaseTest {

    private lateinit var classUnderTest: GetAllPointsUsecase

    private lateinit var mockRepository: TapYouRepository

    @Before
    fun setUp() {
        mockRepository = mock(TapYouRepository::class.java)
        classUnderTest = GetAllPointsUsecase(mockRepository)
    }

    @Test
    fun `execute should return expected result from repository`() = runTest {
        val expectedPoints = listOf(Point(1f, 2f), Point(2f, 3f), Point(3f,4f))
        val expectedResult = Result.success(expectedPoints)
        `when`(mockRepository.getPointsFromApi(3)).thenReturn(expectedResult)


        val result = classUnderTest.execute(3)


        assertEquals(expectedResult, result)

        verify(mockRepository).getPointsFromApi(3)
    }

    @Test
    fun `execute should handle repository failure`() = runTest {
        val exception = Exception("Network error")
        val expectedResult = Result.failure<List<Point>>(exception)
        `when`(mockRepository.getPointsFromApi(3)).thenReturn(expectedResult)

        val result = classUnderTest.execute(3)


        assertEquals(expectedResult, result)
        verify(mockRepository).getPointsFromApi(3)
    }
}