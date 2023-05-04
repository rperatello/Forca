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
import br.com.rperatello.viewmodel.getWordToShow
import java.text.Normalizer

class MainFragment : Fragment() {

    private lateinit var fragmentMainViewBinding: FragmentMainBinding
    private lateinit var forcaViewModel: ForcaViewModel

    private val attemptsImage = arrayOf<Int>(R.mipmap.f0, R.mipmap.f1, R.mipmap.f2, R.mipmap.f3, R.mipmap.f4, R.mipmap.f5, R.mipmap.f6)

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
                    if(it.roundState.ordinal == 2){

                        fragmentMainViewBinding.keyboard.visibility = View.VISIBLE

                        // Todo tela - renderizar dados round state
                        Log.v(getString(R.string.app_name), "entrou ForcaViewModelState.Game")
                        Log.v(getString(R.string.app_name), forcaViewModel.forcaViewModelState.value.toString())
                        Log.v(getString(R.string.app_name), "Palavra: " + it.word.palavra)
    //                    fragmentMainViewBinding.inputTxt.text = it.word.getWordToShow("".toSet())
                        Log.v(getString(R.string.app_name), "WordToShow: " + it.wordToShow)
                        Log.v(getString(R.string.app_name), "WordWithRemoveAccent: " + it.wordWithRemoveAccent)
    //                    fragmentMainViewBinding.inputTv.text = it.wordToShow
                        fragmentMainViewBinding.inputTxtTv.text = it.wordToShow
                        fragmentMainViewBinding.wordTv.text = it.word.palavra

                        fragmentMainViewBinding.gameStatus.setImageResource(attemptsImage[it.attempts])
                    }
                    else {
                        fragmentMainViewBinding.keyboard.visibility = View.GONE
                    }
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
//        fragmentMainViewBinding.resultSection.visibility = View.GONE
//        fragmentMainViewBinding.btStart.visibility = View.GONE
        fragmentMainViewBinding.keyboard.visibility = View.VISIBLE
        fragmentMainViewBinding.wordTv.visibility = View.VISIBLE
        fragmentMainViewBinding.inputTxtTv.visibility = View.VISIBLE
        fragmentMainViewBinding.Section2.visibility = View.VISIBLE

        clickKeyboard()
        forcaViewModel.startGame()
    }

    private fun clickKeyboard(){
        with(fragmentMainViewBinding){
            QbT.setOnClickListener { forcaViewModel.inputLetter('Q'); QbT.isEnabled = false }
            WbT.setOnClickListener { forcaViewModel.inputLetter('W'); WbT.isEnabled = false }
            EbT.setOnClickListener { forcaViewModel.inputLetter('E'); EbT.isEnabled = false }
            RbT.setOnClickListener { forcaViewModel.inputLetter('R'); RbT.isEnabled = false  }
            TbT.setOnClickListener { forcaViewModel.inputLetter('T'); TbT.isEnabled = false  }
            YbT.setOnClickListener { forcaViewModel.inputLetter('Y'); YbT.isEnabled = false  }
            UbT.setOnClickListener { forcaViewModel.inputLetter('U'); UbT.isEnabled = false  }
            IbT.setOnClickListener { forcaViewModel.inputLetter('I'); IbT.isEnabled = false  }
            ObT.setOnClickListener { forcaViewModel.inputLetter('O'); ObT.isEnabled = false  }
            PbT.setOnClickListener { forcaViewModel.inputLetter('P'); PbT.isEnabled = false  }

            AbT.setOnClickListener { forcaViewModel.inputLetter('A'); AbT.isEnabled = false  }
            SbT.setOnClickListener { forcaViewModel.inputLetter('S'); SbT.isEnabled = false  }
            DbT.setOnClickListener { forcaViewModel.inputLetter('D'); DbT.isEnabled = false  }
            FbT.setOnClickListener { forcaViewModel.inputLetter('F'); FbT.isEnabled = false  }
            GbT.setOnClickListener { forcaViewModel.inputLetter('G'); GbT.isEnabled = false  }
            HbT.setOnClickListener { forcaViewModel.inputLetter('H'); HbT.isEnabled = false  }
            JbT.setOnClickListener { forcaViewModel.inputLetter('J'); JbT.isEnabled = false  }
            KbT.setOnClickListener { forcaViewModel.inputLetter('K'); KbT.isEnabled = false  }
            LbT.setOnClickListener { forcaViewModel.inputLetter('L'); LbT.isEnabled = false  }

            ZbT.setOnClickListener { forcaViewModel.inputLetter('Z'); ZbT.isEnabled = false  }
            XbT.setOnClickListener { forcaViewModel.inputLetter('X'); XbT.isEnabled = false  }
            CbT.setOnClickListener { forcaViewModel.inputLetter('C'); CbT.isEnabled = false  }
            VbT.setOnClickListener { forcaViewModel.inputLetter('V'); VbT.isEnabled = false  }
            BbT.setOnClickListener { forcaViewModel.inputLetter('B'); BbT.isEnabled = false  }
            NbT.setOnClickListener { forcaViewModel.inputLetter('N'); NbT.isEnabled = false  }
            MbT.setOnClickListener { forcaViewModel.inputLetter('M'); MbT.isEnabled = false  }
        }
    }


}