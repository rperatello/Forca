package br.com.rperatello.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentMainBinding
import br.com.rperatello.model.settings.GameSettings
import br.com.rperatello.viewmodel.ForcaViewModel

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

    private var rounds = 0
    private var level = 0

    private var hits = 0
    private var defeats = 0
    private var hits_list : List<String> = emptyList()
    private var defeats_list : List<String> = emptyList()


    private var gameSettings =
        GameSettings(1, 1)
    private var gameSettingsBackup =
        GameSettings(1, 1)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().applicationContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        gameSettings.level = sharedPreferences.getInt(GAME_LEVEL, 1)
        gameSettings.round = sharedPreferences.getInt(NUMBER_OF_ROUNDS, 1)

//        forcaViewModel = ViewModelProvider
//            .AndroidViewModelFactory(this.application)
//            .create(ForcaViewModel::class.java)

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
        setNewMatch()
        return
    }

    private fun setNewMatch() {
        Log.v(getString(R.string.app_name), "Entrou setNewMatch")
        //TODO("Chamar rest para obter palavras")
        forcaViewModel.getIdListByLevel(gameSettings.level)
        //TODO("Criar método para fornecer a palavra")
        //TODO("tornar a palavra visível")
        //TODO("tornar a palavra visível")
        //cleanAll()
        return
    }


}