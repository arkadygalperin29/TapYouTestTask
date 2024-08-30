package com.dev.agalperin.domain

import org.junit.Before

import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllPointsUsecaseTest {

    private lateinit var classUnderTest: GetAllPointsUsecase

    @Mock
    private lateinit var mockRepository: TapYouRepository

    @Before
    fun setUp() {
        classUnderTest = GetAllPointsUsecase(mockRepository)
    }

    @Test
    fun execute() {

    }
}