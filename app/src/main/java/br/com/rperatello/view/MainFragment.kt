package br.com.rperatello.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentMainBinding
import br.com.rperatello.model.entity.Word
import br.com.rperatello.model.settings.GameSettings
import br.com.rperatello.viewmodel.ForcaViewModel
import java.lang.Exception

class MainFragment : Fragment() {

    private lateinit var  fragmentMainViewBinding : FragmentMainBinding
    private lateinit var forcaViewModel: ForcaViewModel

    // TODO: Rename and change types of parameters

    val EXTRA_CONFIGURACOES = "EXTRA_SETTINGS"

    val NUMBER_OF_ROUNDS = "NUMBER_OF_ROUNDS"
    val GAME_LEVEL = "GAME_LEVEL"
    val NUM_HITS = "NUM_HITS"
    val HITS_LIST = "HITS_LIST"
    val NUM_DEFEATS = "NUM_DEFEATS"
    val DEFEATS_LIST = "DEFEATS_LIST"

    lateinit var sharedPreferences: SharedPreferences

    private var gameInExecution = false
    private var matchInExecution = false

    private var rounds = 0
    private var level = 0
    private var wordIdList : MutableList<Int> = ArrayList()
    private var word : String = ""

    private var hits = 0
    private var defeats = 0
    private var hits_list : List<String> = emptyList()
    private var defeats_list : List<String> = emptyList()

    private var gameSettings =
        GameSettings(1, 1)
    private var gameSettingsBackup =
        GameSettings(1, 1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().applicationContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        gameSettings.level = sharedPreferences.getInt(GAME_LEVEL, 1)
        gameSettings.round = sharedPreferences.getInt(NUMBER_OF_ROUNDS, 1)

        forcaViewModel = ViewModelProvider(this).get(ForcaViewModel::class.java)

        fragmentMainViewBinding.btStart.setOnClickListener{
            startGame();
            Log.v(getString(R.string.app_name), "onClick - passou startGame");
        }

        return fragmentMainViewBinding.root
    }

    fun startGame() {
        Log.v(getString(R.string.app_name), "Entrou startGame")
        fragmentMainViewBinding.resultSection.setVisibility(View.GONE)
        fragmentMainViewBinding.btStart.setVisibility(View.GONE)
        fragmentMainViewBinding.keyboard.setVisibility(View.VISIBLE)
        fragmentMainViewBinding.resultTxt.setVisibility(View.VISIBLE)

        forcaViewModel.getIdListByLevel(gameSettings.level)

        Log.v(getString(R.string.app_name), "wordIdList - first: $wordIdList")

        forcaViewModel.wordIdListMLD.observe(this){
            forcaViewModel.getIdList(gameSettings.round)
        }

        forcaViewModel.wordIdMLD.observe(this){
            forcaViewModel.getWordById(forcaViewModel.wordIdList[rounds])
        }

        forcaViewModel.wordMLD.observe(this){ res ->
            Log.v(getString(R.string.app_name), "res: $res")
        }
        gameInExecution = true

        while (gameInExecution && rounds < gameSettings.round){
            try{
                Log.v(getString(R.string.app_name), "Entrou while | gameInExecution: $gameInExecution | rounds: $rounds ")
//                Toast.makeText(this@MainFragment.context, "Rodada nº: " + rounds + 1, Toast.LENGTH_SHORT).show()
                Log.v(getString(R.string.app_name), "Rodada nº: " + (rounds + 1))
                rounds += 1

            }
            catch(e: Exception){
                Log.e(getString(R.string.app_name), "startGame - "+ e.message.toString())
                gameInExecution = false
                return
            }
        }
        return
    }

    private fun GetWord() {
        forcaViewModel.wordIdMLD.observe(this){
            forcaViewModel.getWordById(forcaViewModel.wordIdList[rounds])
        }

        forcaViewModel.wordMLD.observe(this){ res ->
//                    setNewMatch(forcaViewModel.wordMLD.value!![0])
            return@observe
        }
        rounds++
    }

    private fun setNewMatch(word: Word) {
        matchInExecution = true
        Log.v(getString(R.string.app_name), "Entrou setNewMatch - Word: $word")
        Log.v(getString(R.string.app_name), "Word: ${word.palavra}")
        Log.v(getString(R.string.app_name), "Tamanho: ${word.letras}")
        //TODO("tornar a palavra visível")
        //cleanAll()
        return
    }


}