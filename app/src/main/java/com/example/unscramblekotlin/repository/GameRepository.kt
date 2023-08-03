package com.example.unscramblekotlin.repository

import android.app.Application
import com.example.unscramblekotlin.data.GameState
import com.example.unscramblekotlin.data.GameStateDataStore

class GameRepository(private val application: Application) {
    fun loadGameState() = GameStateDataStore.loadGameState(application)
    suspend fun saveGameState(state: GameState) = GameStateDataStore.saveGameState(application, state)
}