package com.example.deptsocaldeal

import app.cash.turbine.test
import com.example.deptsocaldeal.data.toDealPresentation
import com.example.deptsocaldeal.domain.InternetAvailability
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.domain.Status
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsAction
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsEffect
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SocialDealsViewModelTest {
    private lateinit var viewModel: SocialDealsViewModel

    private val mockDealsRepository = mockk<Repository>(relaxed = true)
    private val mockInternetAvailability = mockk<InternetAvailability>(relaxed = true)

    @BeforeEach
    fun setup() {
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = SocialDealsViewModel(
            dealsRepository = mockDealsRepository,
            internetAvailability = mockInternetAvailability
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given user lost internet user is prompt with no internet banner`() = runTest {
        every { mockInternetAvailability.internetConnection } returns flowOf(Status.Lost)
        initViewModel()
        advanceUntilIdle()
        viewModel.screenState.test {
            assertEquals(awaitItem().hasInternet, Status.Lost)
        }
    }

    @Test
    fun `given user clicks on item, he is navigated to Deal Details screen`() = runTest {
        viewModel.effect.test {
            viewModel.setAction(SocialDealsAction.NavigateToDealDetailsAction("123"))
            val item = awaitItem()
            assertTrue(item is SocialDealsEffect.NavigateToDetailsEffect)
            assertTrue(item.id == "123")
        }
    }

    @Test
    fun `given user clicks on add deal when the deal `() = runTest {
        viewModel.setAction(SocialDealsAction.ToggleFavoriteAction(testDealEntity.toDealPresentation(), isFavorite = true))
        coVerify {
            mockDealsRepository.addOrRemoveFromFavorites(testDealEntity.toDealPresentation(), true)
        }
    }
}
