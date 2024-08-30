package com.dev.agalperin.domain

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllPointsUsecaseTest {

    private lateinit var usecase: GetAllPointsUsecase
    private val repository: TapYouRepository = mock(TapYouRepository::class.java)

//    @BeforeEach
//    fun setUp() {
//        usecase = GetAllPointsUsecase(repository)
//    }

    @Test
    fun execute() {

    }
}