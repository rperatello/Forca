package br.com.rperatello.model.settings

import android.content.Context
import android.content.SharedPreferences

private const val SETTINGS_NAME = "SHARED_PREF"

private const val SETTINGS_DIFFICULTY = "SETTINGS_DIFFICULTY"
private const val SETTINGS_TOTAL_ROUNDS = "SETTINGS_TOTAL_ROUNDS"

class GameSettings private constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {

        private lateinit var gameSettings: GameSettings

        fun init(context: Context) {
            val pref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
            this.gameSettings = GameSettings(pref)
        }

        fun getInstance(): GameSettings {
            return gameSettings
        }
    }

    var difficulty: Difficulty
        get() {
            val savedId = sharedPreferences.getInt(SETTINGS_DIFFICULTY, 1)
            return Difficulty.findById(savedId)
        }
        set(value) = sharedPreferences.edit().putInt(SETTINGS_DIFFICULTY, value.id).apply()

    var totalRounds: Int
        get() = sharedPreferences.getInt(SETTINGS_TOTAL_ROUNDS, 1)
        set(value) = sharedPreferences.edit().putInt(SETTINGS_TOTAL_ROUNDS, value).apply()

    enum class Difficulty(val id: Int) {
        EASY(1),
        MEDIUM(2),
        HARD(3);

        companion object {
            fun findById(id: Int): Difficulty {
                return Difficulty.values().first { it.id == id }
            }
        }
    }
}