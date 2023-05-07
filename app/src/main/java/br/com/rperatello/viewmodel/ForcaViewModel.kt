package br.com.rperatello.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.rperatello.model.entity.Word
import br.com.rperatello.model.service.Service.serviceApi
import br.com.rperatello.model.settings.GameSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.regex.Pattern

private const val MAX_ATTEMPTS = 6

class ForcaViewModel(application: Application) : AndroidViewModel(application) {

    private var _forcaViewModelState =
        MutableLiveData<ForcaViewModelState>(ForcaViewModelState.Loading)

    val forcaViewModelState: LiveData<ForcaViewModelState>
        get() = _forcaViewModelState

    private var gameSettings = GameSettings.getInstance()
    private var totalRounds = gameSettings.totalRounds

    private var wordIdListByLevel: List<Int>? = null
    private var gameHistory: MutableList<ForcaViewModelState.Game> = mutableListOf()

    fun startGame() {
        viewModelScope.launch() {
            start()
        }
    }

    private suspend fun start() = withContext(Dispatchers.IO) {
        wordIdListByLevel = null
        gameHistory.clear()

        val difficulty = gameSettings.difficulty
        val wordIdListByLevel = getWordIdListByLevel(difficulty.id)?.shuffled()

        if (wordIdListByLevel == null) {
            _forcaViewModelState.postValue(ForcaViewModelState.Error("Não foi possível recuperar a lista de ids das palavras"))
        } else {
            this@ForcaViewModel.wordIdListByLevel = wordIdListByLevel

            getWordAndUpdateState(wordIdListByLevel, 1)
        }
    }

    private suspend fun getWordIdListByLevel(id: Int): ArrayList<Int>? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                serviceApi.retrieveIdentifiers(id)
            } catch (e: Exception) {
                Log.e("Jogo da Forca", "Exception: $e")
                null
            }
        }

    private fun getWordAndUpdateState(wordIdListByLevel: List<Int>, round: Int) {
        viewModelScope.launch() {
            val wordId = wordIdListByLevel[round]
            val word = getWordById(wordId)

            if (word == null) {
                _forcaViewModelState.postValue(ForcaViewModelState.Error("Não foi possível recuperar a palavra"))
            } else {
                _forcaViewModelState.postValue(
                    ForcaViewModelState.Game(
                        currentRound = round,
                        word = word,
                        wordWithRemoveAccent = word.palavra.uppercase().removeAccent(),
                        wordToShow = word.getWordToShow(setOf()).uppercase(),
                        attempts = 0,
                        inputLetters = emptySet(),
                        roundState = RoundState.PLAYING
                    )
                )
            }
        }
    }

    private suspend fun getWordById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext try {
            serviceApi.retrieveWord(id).first()
        } catch (e: Exception) {
            Log.e("Jogo da Forca", "Exception: $e")
            null
        }
    }

    fun inputLetter(char: Char) {
        val currentState = _forcaViewModelState.value as? ForcaViewModelState.Game ?: return

        val inputLetters = currentState.inputLetters.toMutableSet()
        inputLetters.add(char)

        val newWordToShow = currentState.word.getWordToShow(inputLetters)
        val hasWordChanged = newWordToShow.uppercase() != currentState.wordToShow.uppercase()
        val roundWon = newWordToShow == currentState.word.palavra.uppercase()

        val attempts = if (!roundWon && !hasWordChanged) {
            currentState.attempts + 1
        } else {
            currentState.attempts
        }

        val roundState = when {
            roundWon -> RoundState.WON
            attempts >= MAX_ATTEMPTS -> RoundState.LOST
            else -> RoundState.PLAYING
        }

        _forcaViewModelState.postValue(
            ForcaViewModelState.Game(
                currentRound = currentState.currentRound,
                word = currentState.word,
                wordWithRemoveAccent = currentState.word.palavra.uppercase().removeAccent(),
                wordToShow = newWordToShow,
                attempts = attempts,
                inputLetters = inputLetters,
                roundState = roundState
            )
        )
    }

    fun nextRound() {
        val currentState = _forcaViewModelState.value as? ForcaViewModelState.Game ?: return

        gameHistory.add(currentState)
        val currentRound = currentState.currentRound + 1

        if (currentRound > totalRounds) {
            val hits = gameHistory.filter { it.roundState == RoundState.WON }
                .map { it.word.palavra }
                .toList()

            val misses = gameHistory.filter { it.roundState == RoundState.LOST }
                .map { it.word.palavra }
                .toList()

            _forcaViewModelState.postValue(ForcaViewModelState.GameOver(hits, misses))
        } else {
            val wordIdListByLevel = this.wordIdListByLevel ?: return
            getWordAndUpdateState(wordIdListByLevel, currentRound)
        }
    }
}

sealed class ForcaViewModelState {
    object Loading : ForcaViewModelState()

    data class Game(
        val currentRound: Int,
        val word: Word,
        val wordWithRemoveAccent: String,
        val wordToShow: String,
        val attempts: Int,
        val inputLetters: Set<Char> = setOf(),
        val roundState: RoundState
    ) : ForcaViewModelState()

    data class GameOver(
        val hits: List<String>,
        val missed: List<String>
    ) : ForcaViewModelState()

    data class Error(val error: String) : ForcaViewModelState()
}

enum class RoundState {
    WON,
    LOST,
    PLAYING
}

fun Word.getWordToShow(inputLetters: Set<Char>): String {
    val wordToShow = StringBuilder()

    for ( (index, letter) in this.palavra.uppercase().removeAccent().withIndex()) {
        if (inputLetters.contains(letter)) {
            wordToShow.append(this.palavra.uppercase()[index])
        } else {
            wordToShow.append("_")
        }
    }

    return wordToShow.toString()
}

fun String.removeAccent(): String {
    val nfdNormalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}
