package com.dev.agalperin.data

import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.TapYouApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TapYouRepositoryImplTest {

    private lateinit var classUnderTest: TapYouRepositoryImpl
    private lateinit var fakeApi: TapYouApi

    @Before
    fun setUp() {
        fakeApi = FakeTapYouApi()
        classUnderTest = TapYouRepositoryImpl(fakeApi)
    }

    @Test
    fun `getPointsFromApi should return mapped data from FakeTapYouApi`() = runTest {
        val count = 3
        val expectedPoints = listOf(
            Point(1.0f, 2.0f),
            Point(3.0f, 4.0f),
            Point(5.0f, 6.0f)
        )

        val result = classUnderTest.getPointsFromApi(count)


        assertEquals(Result.success(expectedPoints), result)
    }
}