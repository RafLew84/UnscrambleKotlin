package com.example.unscramblekotlin.data

import com.example.unscramblekotlin.ui.GameUiState

data class GameState (
    val gameUiState: GameUiState,
    val usedWords: Set<String>,
    val currentWord: String
)