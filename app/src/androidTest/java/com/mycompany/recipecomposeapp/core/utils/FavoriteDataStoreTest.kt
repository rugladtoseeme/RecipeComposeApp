package com.mycompany.recipecomposeapp.core.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteDataStoreTest {

    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)
        assertTrue(manager.getFavoriteIdsFlow().first().contains("42"))
    }

    @Test
    fun removeFromFavoritesDeletesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)
        assertTrue(manager.getFavoriteIdsFlow().first().contains("42"))

        manager.removeFavorite(recipeId = 42)
        assertFalse(manager.getFavoriteIdsFlow().first().contains("42"))
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        assertFalse(manager.getFavoriteIdsFlow().first().contains("42"))

        manager.getFavoriteIdsFlow().test {
            awaitItem()
            manager.addFavorite(42)
            assertTrue(manager.getFavoriteIdsFlow().first().contains("42"))
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(manager.getFavoriteIdsFlow().first().contains("42"))
    }
}