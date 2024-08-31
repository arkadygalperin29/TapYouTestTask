package com.dev.agalperin.data

import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.TapYouApi
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class TapYouRepositoryImplTest {

    private lateinit var classUnderTest: TapYouRepositoryImpl
    private lateinit var fakeApi: TapYouApi

    @Before
    fun setUp() {
        fakeApi = mock(TapYouApi::class.java)
        classUnderTest = TapYouRepositoryImpl(fakeApi)
    }

    @Test
    fun `getPointsFromApi should return mapped data from FakeTapYouApi`() = runTest {
        val count = 3
        val initialPoints = listOf(
            PointDto(1.0, 2.0),
            PointDto(3.0, 4.0),
            PointDto(5.0, 6.0)
        )
        val pointsResponse = PointsResponse(initialPoints)

        `when`(fakeApi.getPoints(count)).thenReturn(pointsResponse)

        val expectedPoints = initialPoints.map { it.toPoint() }

        val result = classUnderTest.getPointsFromApi(count)

        assertEquals(Result.success(expectedPoints), result)
    }
}