package com.dev.agalperin.data

import com.dev.agalperin.domain.model.PointTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TapYouRepositoryImplTest {

    private lateinit var mockRepositoryImpl: FakeTapYouRepositoryImpl

    @Before
    fun setUp() {
        mockRepositoryImpl = FakeTapYouRepositoryImpl()
    }

    @Test
    fun `test getPointsFromApi returns correct data`() = runTest {
        val expectedPoints = listOf(
            PointTest(x = 5.0, y = 5.0),
            PointTest(x = 5.0, y = 7.0),
            PointTest(x = 8.0, y = 4.0),
            PointTest(x = 10.0, y = 7.0),
            PointTest(x = 13.0, y = 11.0)
        )

        val response = mockRepositoryImpl.getPointsFromApi(expectedPoints.size)

        assertEquals(expectedPoints, response.pointsList)
    }

    @Test
    fun `test getPointsFromApi returns different data when coordinates are different`() = runTest {
        val expectedPoints = listOf(
            PointTest(x = 5.0, y = 5.1),
            PointTest(x = 5.0, y = 7.0),
            PointTest(x = 8.1, y = 4.0),
            PointTest(x = 10.0, y = 7.0),
            PointTest(x = 13.0, y = 11.0)
        )

        val response = mockRepositoryImpl.getPointsFromApi(expectedPoints.size)

        assertFalse(expectedPoints == response.pointsList)
    }

    @Test
    fun `test getPointsFromApi returns different data when length is different`() = runTest {
        val expectedPoints = listOf(
            PointTest(x = 5.0, y = 5.0),
            PointTest(x = 5.0, y = 7.0),
            PointTest(x = 8.0, y = 4.0)
        )

        val response = mockRepositoryImpl.getPointsFromApi(expectedPoints.size)

        assertFalse(expectedPoints == response.pointsList)
    }
}