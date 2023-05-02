package br.com.rperatello.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentMainBinding
import br.com.rperatello.viewmodel.ForcaViewModel
import br.com.rperatello.viewmodel.ForcaViewModelState

class MainFragment : Fragment() {

    private lateinit var fragmentMainViewBinding: FragmentMainBinding
    private lateinit var forcaViewModel: ForcaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        forcaViewModel = ViewModelProvider(this).get(ForcaViewModel::class.java)

//        fragmentMainViewBinding.btStart.setOnClickListener {
//            Log.v(getString(R.string.app_name), "onClick - passou startGame")
//            startGame()
//        }

        Log.v(getString(R.string.app_name), "call startGame()")
        startGame()

        listenToViewModelState()

        return fragmentMainViewBinding.root
    }

    private fun listenToViewModelState() {
        forcaViewModel.forcaViewModelState.observe(this) {
            when (it) {
                ForcaViewModelState.Loading -> {
                    // Todo loading
                    Log.v(getString(R.string.app_name), "entrou ForcaViewModelState.Loading")
                }

                is ForcaViewModelState.Game -> {
                    // Todo tela - renderizar dados round state
                    Log.v(getString(R.string.app_name), "entrou ForcaViewModelState.Game")
                    Log.v(getString(R.string.app_name), forcaViewModel.forcaViewModelState.value.toString())
                    Log.v(getString(R.string.app_name), "Palavra: " + it.word.palavra)
                }

                is ForcaViewModelState.GameOver -> {
                    // Todo mostrar relatório
                    Log.v(getString(R.string.app_name), "entrou ForcaViewModelState.GameOver")
                }

                is ForcaViewModelState.Error -> {
                    // Todo mostrar erro
                    Log.v(getString(R.string.app_name), "entrou ForcaViewModelState.Error")
                    Log.e(getString(R.string.app_name), it.error)
                }
            }
        }
    }

    private fun startGame() {
        Log.v(getString(R.string.app_name), "Entrou startGame")
        fragmentMainViewBinding.resultSection.visibility = View.GONE
//        fragmentMainViewBinding.btStart.visibility = View.GONE
        fragmentMainViewBinding.keyboard.visibility = View.VISIBLE
        fragmentMainViewBinding.resultTxt.visibility = View.VISIBLE

        forcaViewModel.startGame()
    }

}