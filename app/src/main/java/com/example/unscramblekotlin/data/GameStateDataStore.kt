package com.example.unscramblekotlin.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unscramblekotlin.ui.GameUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object GameStateDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("game_state_kotlin")

    private val CURRENT_SCRAMBLED_WORD_KEY = stringPreferencesKey("current_scrambled_word")
    private val CURRENT_WORD_COUNT_KEY = intPreferencesKey("current_word_count")
    private val SCORE_KEY = intPreferencesKey("score")
    private val IS_GUESSED_WORD_WRONG_KEY = booleanPreferencesKey("is_guessed_word_wrong")
    private val IS_GAME_OVER_KEY = booleanPreferencesKey("is_game_over")
    private val USED_WORDS_KEY = stringSetPreferencesKey("used_words")
    private val CURRENT_WORD_KEY = stringPreferencesKey("current_word")

    suspend fun saveGameState(context: Context, gameState: GameState) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_SCRAMBLED_WORD_KEY] = gameState.gameUiState.currentScrambledWord
            preferences[CURRENT_WORD_COUNT_KEY] = gameState.gameUiState.currentWordCount
            preferences[SCORE_KEY] = gameState.gameUiState.score
            preferences[IS_GUESSED_WORD_WRONG_KEY] = gameState.gameUiState.isGuessedWordWrong
            preferences[IS_GAME_OVER_KEY] = gameState.gameUiState.isGameOver
            preferences[USED_WORDS_KEY] = gameState.usedWords
            preferences[CURRENT_WORD_KEY] = gameState.currentWord
        }
    }

    fun loadGameState(context: Context): Flow<GameState> {
        return context.dataStore.data
            .catch { exception ->
                // DataStore throws an IOException if an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val currentScrambledWord = preferences[CURRENT_SCRAMBLED_WORD_KEY] ?: ""
                val currentWordCount = preferences[CURRENT_WORD_COUNT_KEY] ?: 1
                val score = preferences[SCORE_KEY] ?: 0
                val isGuessedWordWrong = preferences[IS_GUESSED_WORD_WRONG_KEY] ?: false
                val isGameOver = preferences[IS_GAME_OVER_KEY] ?: false
                val usedWordsSet = preferences[USED_WORDS_KEY] ?: emptySet()
                val currentWord = preferences[CURRENT_WORD_KEY] ?: ""

                GameState(
                    GameUiState(
                        currentScrambledWord,
                        currentWordCount,
                        score,
                        isGuessedWordWrong,
                        isGameOver
                    ),
                    usedWordsSet.toMutableSet(),
                    currentWord
                )
            }
    }
}