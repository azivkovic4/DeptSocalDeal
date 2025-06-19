package com.example.deptsocaldeal

import app.cash.turbine.test
import com.example.deptsocaldeal.data.local.DealsDatabase
import com.example.deptsocaldeal.data.services.DealsApi
import com.example.deptsocaldeal.domain.HtmlMapperUseCase
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.domain.RepositoryImpl
import com.example.deptsocaldeal.util.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RepositoryTest {
    private lateinit var repository: Repository

    private val mockDealsApi = mockk<DealsApi>(relaxed = true)
    private val mockDealsDatabase = mockk<DealsDatabase>(relaxed = true)
    private val mockHtmlMapperUseCase = mockk<HtmlMapperUseCase>(relaxed = true) {
        every { this@mockk(any()) } returns "Mapped HTML"
    }

    private fun initRepository() {
        repository = RepositoryImpl(
            dealsApi = mockDealsApi,
            dealsDatabase = mockDealsDatabase,
            htmlMapper = mockHtmlMapperUseCase
        )
    }

    @BeforeEach
    fun setup() {
        initRepository()
    }

    @Test
    fun `given discoverDeals is called always initial state is Loading`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(listOf(testDealEntity))
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
        )
        coEvery { mockDealsApi.discoverDeals() } returns testDealsResponse
        initRepository()
        repository.discoverDeals().test {
            assertTrue(awaitItem() is Result.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given user chosen USD, has one favorite deal, successful data with dose predicate is expected`() =
        runTest {
            every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(
                listOf(
                    testDealEntity
                )
            )
            every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
                testPreferredCurrencyEntity
            )
            coEvery { mockDealsApi.discoverDeals() } returns testDealsResponse
            initRepository()
            this.advanceUntilIdle()
            repository.discoverDeals().test {
                skipItems(1)
                val result = expectMostRecentItem() as Result.Success
                assertTrue(result.data.isNotEmpty())
                assertTrue(result.data.count { it.isFavorite } == 1)
                assertTrue(result.data.all { it.symbol == "$" })
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given user chosen EUR, has one favorite deal, successful data with dose predicate is expected`() =
        runTest {
            every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(
                listOf(
                    testDealEntity
                )
            )
            every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
                testPreferredCurrencyEntity.copy(
                    symbol = "€"
                )
            )
            coEvery { mockDealsApi.discoverDeals() } returns testDealsResponse
            initRepository()
            this.advanceUntilIdle()
            repository.discoverDeals().test {
                skipItems(1)
                val result = expectMostRecentItem() as Result.Success
                assertTrue(result.data.isNotEmpty())
                assertTrue(result.data.count { it.isFavorite } == 1)
                assertTrue(result.data.all { it.symbol == "€" })
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given server returns error, data is wrapped in Error response`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(listOf(testDealEntity))
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
        )
        coEvery { mockDealsApi.discoverDeals() } throws Exception("Network Error")
        initRepository()
        this.advanceUntilIdle()
        repository.discoverDeals().test {
            skipItems(1)
            assertTrue(expectMostRecentItem() is Result.Error)
        }
    }

    @Test
    fun `given dealDetails is called always initial state is Loading`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(listOf(testDealEntity))
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
        )
        coEvery { mockDealsApi.dealDetails("test") } returns testDealDto
        initRepository()
        repository.discoverDeals().test {
            assertTrue(awaitItem() is Result.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given dealDetails is called, current currency is USD and is favorite deal`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(listOf(testDealEntity))
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
        )
        coEvery { mockDealsApi.dealDetails("test") } returns testDealDto
        initRepository()
        repository.dealDetails("test").test {
            skipItems(1)
            val result = expectMostRecentItem() as Result.Success
            assertTrue(result.data.isFavorite)
            assertTrue(result.data.symbol == "$")
        }
    }

    @Test
    fun `given dealDetails is called, current currency is EUR and the deal is not favorite`() =
        runTest {
            every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(
                listOf(
                    testDealEntity
                )
            )
            every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
                testPreferredCurrencyEntity.copy(symbol = "€")
            )
            coEvery { mockDealsApi.dealDetails("test") } returns testDealDto.copy(unique = "31231")
            initRepository()
            repository.dealDetails("test").test {
                skipItems(1)
                val result = expectMostRecentItem() as Result.Success
                assertFalse((result.data.isFavorite))
                assertTrue(result.data.symbol == "€")
            }
        }

    @Test
    fun `given favoriteDeals is called, current currency is EUR`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(
            listOf(
                testDealEntity.copy(isFavorite = true),
                testDealEntity.copy(id = "3124", isFavorite = true),
                testDealEntity.copy(id = "312424", isFavorite = true)
            )
        )
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
                .copy(symbol = "€")
        )
        initRepository()
        repository.favoriteDeals().test {
            val item = awaitItem()
            assertTrue(item.all { it.isFavorite })
            assertTrue(item.all { it.symbol == "€" })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given favoriteDeals is called, current currency is USD`() = runTest {
        every { mockDealsDatabase.dealsDao.getFavoriteDeals() } returns flowOf(
            listOf(
                testDealEntity.copy(isFavorite = true),
                testDealEntity.copy(id = "3124", isFavorite = true),
                testDealEntity.copy(id = "312424", isFavorite = true)
            )
        )
        every { mockDealsDatabase.dealsDao.getPreferredCurrency() } returns flowOf(
            testPreferredCurrencyEntity
        )
        initRepository()
        repository.favoriteDeals().test {
            val item = awaitItem()
            assertTrue(item.all { it.isFavorite })
            assertTrue(item.all { it.symbol == "$" })
            cancelAndIgnoreRemainingEvents()
        }
    }
}
